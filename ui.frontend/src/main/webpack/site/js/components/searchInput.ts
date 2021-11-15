import debounce from '../utils/debounce'
import fetchAutoSuggest from '../utils/fetchAutoSuggest'

type SearchInputOptions = {
  searchFieldPlaceholderText: string
  autosuggestUrl: string
  autocompleteTriggerThreshold: number
  autoSuggestionDebounceTime: number
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

      if (query.length >= autocompleteTriggerThreshold) {
        const results = await fetchAutoSuggest(autosuggestUrl, query)

        if (results?.length) {
          const dataListElement = document.createElement('div')
          dataListElement.id = 'suggestions'

          results.forEach((result) => {
            const dataListOptionElement = document.createElement('div')
            dataListOptionElement.innerText = result
            dataListOptionElement.classList.add('saas-suggestions-element')

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

  searchInput.addEventListener('input', (event) => {
    debouncedSearch(autoSuggestionDebounceTime)(
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

  return searchInput
}

export default buildSearchInput
