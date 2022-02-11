import { QUERY_PARAM_SEARCH_TERM } from '../constants'
import cleanString from '../utils/cleanString'
import debounce from '../utils/debounce'
import fetchAutoComplete from '../utils/fetchAutoComplete'
import {
  cleanSessionStorage,
  getSessionStorage,
  setSessionStorage,
  STORAGE_QUERY_STRING_KEY,
  STORAGE_SUGGESTIONS_KEY,
} from '../utils/sessionStorage'

type SearchInputOptions = {
  id: string
  searchFieldPlaceholderText: string
  autocompleteUrl: string
  autocompleteTriggerThreshold: number
  autoSuggestionDebounceTime: number
  searchContainer: HTMLDivElement
}
const SUGGESTION_DROPDOWN_ID = 'suggestions'
const SEARCH_INPUT_CLASS = 'search-input'
const SAAS_CONTAINER_FORM_SUGGESTIONS_CLASS = '.cmp-saas__form #suggestions'
const SUGGESTION_ELEMENT_CLASS = 'cmp-saas__suggestion-element'
const ACTIVE_SUGGESTION_ELEMENT_CLASS = `${SUGGESTION_ELEMENT_CLASS}--active`

const setSaasCurrentFocusSuggestion = (
  inputElement: HTMLInputElement,
  value: number,
) => {
  inputElement.setAttribute('saasCurrentFocusSuggestion', value.toString())
}

const getSaasCurrentFocusSuggestion = (
  inputElement: HTMLInputElement,
): string => {
  return inputElement.getAttribute('saasCurrentFocusSuggestion') ?? '-1'
}

const removeSuggestionList = (searchContainer: HTMLDivElement) => {
  const existingDataList = searchContainer.querySelector(
    SAAS_CONTAINER_FORM_SUGGESTIONS_CLASS,
  )

  if (existingDataList) {
    existingDataList.remove()
  }
}

const submitSearchForm = (): void => {
  const form = document.querySelector('.cmp-saas__form') as
    | HTMLFormElement
    | undefined
  form?.submit()
}

const buildSuggestionElements = ({
  results,
  regexp,
  query,
  searchInput,
  searchContainer,
  suggestionDropdown,
}: {
  results: string[]
  regexp: RegExp
  query: string
  searchInput: HTMLInputElement
  searchContainer: HTMLDivElement
  suggestionDropdown: Element
}): void => {
  if (!query) {
    return
  }

  const searchButtonElement = document.getElementsByClassName(
    'cmp-saas__button',
  )?.[0] as HTMLButtonElement | undefined
  results.forEach((result) => {
    const cleanAndFormatResult = cleanString(result).replace(
      regexp,
      `<span class="cmp-saas__suggestion-element--matched-query">${query}</span>`,
    )
    const suggestionDropdownElement = document.createElement('div')
    suggestionDropdownElement.innerHTML = cleanAndFormatResult
    suggestionDropdownElement.classList.add(SUGGESTION_ELEMENT_CLASS)

    suggestionDropdownElement.addEventListener('click', () => {
      const searchInputElementCopy = searchInput
      cleanSessionStorage()
      removeSuggestionList(searchContainer)

      searchInputElementCopy.value = result

      if (searchButtonElement) {
        searchButtonElement.click()
      } else {
        submitSearchForm()
      }
    })

    suggestionDropdown.appendChild(suggestionDropdownElement)
  })
  searchInput.after(suggestionDropdown)
}

const getCleanedQueryAndRegex = (
  query: string,
): { cleanedQuery: string; regexp: RegExp } => {
  const cleanedQuery = cleanString(query)
  const regexp = new RegExp(cleanedQuery, 'gi')

  return { cleanedQuery, regexp }
}

const debouncedSearch = (autoSuggestionDebounceTime: number) =>
  debounce(
    async (
      autocompleteUrl: string,
      query: string,
      autocompleteTriggerThreshold: number,
      searchInput: HTMLInputElement,
      searchContainer: HTMLDivElement,
    ) => {
      setSaasCurrentFocusSuggestion(searchInput, -1)

      if (!query?.length || query.length < autocompleteTriggerThreshold) {
        removeSuggestionList(searchContainer)
        cleanSessionStorage()
      }

      if (query.length >= autocompleteTriggerThreshold) {
        const { cleanedQuery, regexp } = getCleanedQueryAndRegex(query)
        const results = await fetchAutoComplete(autocompleteUrl, cleanedQuery)
        const existingSuggestions = searchContainer.querySelector(
          SAAS_CONTAINER_FORM_SUGGESTIONS_CLASS,
        )
        let suggestionDropdown: Element | null = null

        setSessionStorage({
          storageKey: STORAGE_QUERY_STRING_KEY,
          data: query,
          stringify: false,
        })

        if (!existingSuggestions) {
          suggestionDropdown = document.createElement('div')
          suggestionDropdown.id = SUGGESTION_DROPDOWN_ID
        } else {
          suggestionDropdown = existingSuggestions
        }

        cleanSessionStorage([STORAGE_SUGGESTIONS_KEY])
        suggestionDropdown.innerHTML = ''

        if (results?.length) {
          setSessionStorage({
            storageKey: STORAGE_SUGGESTIONS_KEY,
            data: results,
          })

          buildSuggestionElements({
            results,
            regexp,
            query,
            searchInput,
            searchContainer,
            suggestionDropdown,
          })
        }
      }
    },
    autoSuggestionDebounceTime,
  )

const buildSearchInput = ({
  id,
  searchFieldPlaceholderText,
  autocompleteUrl,
  autocompleteTriggerThreshold,
  autoSuggestionDebounceTime = 500,
  searchContainer,
}: SearchInputOptions): HTMLInputElement => {
  const searchInput = document.createElement('input')
  searchInput.classList.add(SEARCH_INPUT_CLASS)
  searchInput.name = QUERY_PARAM_SEARCH_TERM

  searchInput.placeholder = searchFieldPlaceholderText
  searchInput.id = id
  searchInput.autocomplete = 'off'
  setSaasCurrentFocusSuggestion(searchInput, -1)

  const search = debouncedSearch(autoSuggestionDebounceTime)

  searchInput.addEventListener('focus', () => {
    const query = getSessionStorage({
      storageKey: STORAGE_QUERY_STRING_KEY,
      defaultValue: '',
      parse: false,
    })
    const results: string[] = getSessionStorage({
      storageKey: STORAGE_SUGGESTIONS_KEY,
      defaultValue: '[]',
    })
    const { regexp } = getCleanedQueryAndRegex(query)
    const suggestionDropdown = document.createElement('div')

    suggestionDropdown.id = SUGGESTION_DROPDOWN_ID

    buildSuggestionElements({
      results,
      regexp,
      query,
      searchInput,
      searchContainer,
      suggestionDropdown,
    })
  })

  searchInput.addEventListener('input', (event) => {
    search(
      autocompleteUrl,
      (event?.target as HTMLInputElement)?.value,
      autocompleteTriggerThreshold,
      searchInput,
      searchContainer,
    )
  })

  document.addEventListener('click', (event) => {
    /* Remove the autocomplete list from DOM when a click happens in the document, except if it is on the search input element */
    if (
      !(event.target as HTMLInputElement).classList.contains(SEARCH_INPUT_CLASS)
    ) {
      removeSuggestionList(searchContainer)
    }
  })

  searchInput.addEventListener('keydown', (e) => {
    const DOWN_ARROW = 'ArrowDown'
    const UP_ARROW = 'ArrowUp'
    const ENTER_KEY = 'Enter'

    const suggestionElements = searchContainer.querySelectorAll<HTMLDivElement>(
      `#suggestions .${SUGGESTION_ELEMENT_CLASS}`,
    )

    if (!suggestionElements.length) {
      return
    }

    const minFocus = -1
    const maxFocus = suggestionElements.length - 1
    const currentFocusAttr = getSaasCurrentFocusSuggestion(searchInput)
    const currentFocus = parseInt(currentFocusAttr, 10)

    const suggestionElement = suggestionElements[currentFocus]

    if (e.key === DOWN_ARROW && currentFocus < maxFocus) {
      const newFocus = currentFocus + 1
      const newSuggestionElement = suggestionElements[newFocus]

      suggestionElement?.classList.remove(ACTIVE_SUGGESTION_ELEMENT_CLASS)
      newSuggestionElement?.classList.add(ACTIVE_SUGGESTION_ELEMENT_CLASS)

      setSaasCurrentFocusSuggestion(searchInput, newFocus)
    }

    if (e.key === UP_ARROW && currentFocus > minFocus) {
      const newFocus = currentFocus - 1
      const newSuggestionElement = suggestionElements[newFocus]

      suggestionElement?.classList.remove(ACTIVE_SUGGESTION_ELEMENT_CLASS)
      newSuggestionElement?.classList.add(ACTIVE_SUGGESTION_ELEMENT_CLASS)

      setSaasCurrentFocusSuggestion(searchInput, newFocus)
    }

    if (e.key === ENTER_KEY && suggestionElement) {
      setSaasCurrentFocusSuggestion(searchInput, -1)
      suggestionElement.click()
    }
  })

  return searchInput
}

export default buildSearchInput
