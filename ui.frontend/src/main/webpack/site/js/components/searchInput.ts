import debounce from '../utils/debounce'
import fetchAutoSuggest from '../utils/fetchAutoSuggest'

type SearchInputOptions = {
  searchFieldPlaceholderText: string
  autosuggestUrl: string
  autocompleteTriggerThreshold: number
  autoSuggestionDebounceTime: number
}

const SUGGESTION_ELEMENT_CLASS = 'saas-suggestions-element'
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

const removeSuggestionList = () => {
  const existingDataList = document.querySelector(
    '.saas-container_form #suggestions',
  )

  if (existingDataList) {
    existingDataList.remove()
  }
}

const debouncedSearch = (autoSuggestionDebounceTime: number) =>
  debounce(
    async (
      autosuggestUrl: string,
      query: string,
      autocompleteTriggerThreshold: number,
      searchInput: HTMLInputElement,
    ) => {
      removeSuggestionList()
      setSaasCurrentFocusSuggestion(searchInput, -1)

      if (query.length >= autocompleteTriggerThreshold) {
        const results = await fetchAutoSuggest(autosuggestUrl, query)

        if (results?.length) {
          const dataListElement = document.createElement('div')
          dataListElement.id = 'suggestions'

          results.forEach((result) => {
            const dataListOptionElement = document.createElement('div')
            dataListOptionElement.innerText = result
            dataListOptionElement.classList.add(SUGGESTION_ELEMENT_CLASS)

            dataListOptionElement.addEventListener('click', () => {
              const searchInputElementCopy = searchInput

              removeSuggestionList()

              searchInputElementCopy.value = result
            })

            dataListElement.appendChild(dataListOptionElement)
          })

          searchInput.after(dataListElement)
        }
      }
    },
    autoSuggestionDebounceTime,
  )

const buildSearchInput = ({
  searchFieldPlaceholderText,
  autosuggestUrl,
  autocompleteTriggerThreshold,
  autoSuggestionDebounceTime = 500,
}: SearchInputOptions): HTMLInputElement => {
  const searchInput = document.createElement('input')

  searchInput.placeholder = searchFieldPlaceholderText
  searchInput.id = 'saas-search-input'
  searchInput.autocomplete = 'off'
  setSaasCurrentFocusSuggestion(searchInput, -1)

  const search = debouncedSearch(autoSuggestionDebounceTime)

  searchInput.addEventListener('input', (event) => {
    search(
      autosuggestUrl,
      (event?.target as HTMLInputElement)?.value,
      autocompleteTriggerThreshold,
      searchInput,
    )
  })

  document.addEventListener('click', () => {
    /* Remove the autocomplete list from DOM when a click happens in the document */
    removeSuggestionList()
  })

  searchInput.addEventListener('keydown', (e) => {
    const DOWN_ARROW = 'ArrowDown'
    const UP_ARROW = 'ArrowUp'
    const ENTER_KEY = 'Enter'

    const suggestionElements = document.querySelectorAll<HTMLDivElement>(
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
