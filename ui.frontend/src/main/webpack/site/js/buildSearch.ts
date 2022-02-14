import buildSearchButton from './components/searchButton'
import buildSearchForm, {addEventToSearchForm, triggerSearch,} from './components/searchForm'
import buildSearchInput from './components/searchInput'
import {QUERY_PARAM_SEARCH_TERM} from './constants'
import {getDataAttributeFromSearchElement} from './searchElement'
import {SearchOptions} from './types/searchOptions'
import initSaasStyle from './utils/saasStyle'

export const buildSearch = async (
    searchElement: HTMLDivElement,
    options?: SearchOptions,
): Promise<void> => {
  const searchConfig = getDataAttributeFromSearchElement(searchElement)

  if (!searchConfig) {
    return
  }

  const { callbacks, autoSuggestionDebounceTime = 500 } = options || {}

  const {
    id,
    searchFieldPlaceholderText,
    searchButtonText,
    searchUrl,
    searchTabs,
    loadMoreButtonText,
    autoCompleteUrl,
    autoCompleteTriggerThreshold,
    autoSuggestText,
    noResultsText,
  } = searchConfig

  const searchContainer = document.createElement('div')
  searchContainer.classList.add('saas-container')

  const searchFormElement = buildSearchForm()

  const searchAutoCompleteWrapper = document.createElement('div')
  searchAutoCompleteWrapper.classList.add('saas-autocomplete')

  const searchInputElement = buildSearchInput({
    id,
    searchFieldPlaceholderText,
    autoCompleteUrl: autoCompleteUrl,
    autoCompleteTriggerThreshold: autoCompleteTriggerThreshold,
    autoSuggestionDebounceTime,
    searchContainer,
  })

  const searchButtonElement = searchButtonText
    ? buildSearchButton({
        searchButtonText,
      })
    : null

  addEventToSearchForm(
      searchFormElement,
      searchInputElement,
      searchUrl,
      searchTabs,
      loadMoreButtonText,
      autoSuggestText,
      searchContainer,
      noResultsText,
      callbacks,
  )

  initSaasStyle()
  searchAutoCompleteWrapper.appendChild(searchInputElement)
  searchFormElement.appendChild(searchAutoCompleteWrapper)
  if (searchButtonElement) {
    searchFormElement.appendChild(searchButtonElement)
  }

  const searchElementParent = searchElement.parentElement
  searchElementParent?.replaceChild(searchContainer, searchElement)

  searchContainer.appendChild(searchFormElement)

  const currentUrl = new URL(window.location.href)
  const currentParams = new URLSearchParams(currentUrl.search)
  const searchValue = currentParams.get(QUERY_PARAM_SEARCH_TERM)

  if (searchValue) {
    searchInputElement.value = searchValue

    await triggerSearch(
      searchFormElement,
      searchInputElement,
      searchUrl,
      searchTabs,
      loadMoreButtonText,
      autoSuggestText,
      searchContainer,
      noResultsText,
      callbacks,
    )
  }
}
