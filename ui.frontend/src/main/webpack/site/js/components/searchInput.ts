import debounce from '../utils/debounce'
import fetchAutoSuggest from '../utils/fetchAutoSuggest'

type SearchInputOptions = {
  searchFieldPlaceholderText: string
  autosuggestUrl: string
  autocompleteTriggerThreshold: number
  autoSuggestionDebounceTime: number
}

const debouncedSearch = (autoSuggestionDebounceTime: number) =>
  debounce(
    async (
      autosuggestUrl: string,
      query: string,
      autocompleteTriggerThreshold: number,
      searchInput: HTMLInputElement,
    ) => {
      if (query.length >= autocompleteTriggerThreshold) {
        const results = await fetchAutoSuggest(autosuggestUrl, query)

        if (results?.length) {
          const existingDataList = document.querySelector(
            '.saas-container_form datalist',
          )

          if (existingDataList) {
            existingDataList.remove()
          }

          const dataListElement = document.createElement('datalist')
          dataListElement.id = 'suggestions'

          results.forEach((result) => {
            const dataListOptionElement = document.createElement('option')
            dataListOptionElement.value = result

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
  searchInput.setAttribute('list', 'suggestions')

  searchInput.addEventListener('input', (event) => {
    debouncedSearch(autoSuggestionDebounceTime)(
      autosuggestUrl,
      (event?.target as HTMLInputElement)?.value,
      autocompleteTriggerThreshold,
      searchInput,
    )
  })

  return searchInput
}

export default buildSearchInput
