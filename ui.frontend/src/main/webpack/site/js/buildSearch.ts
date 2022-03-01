import { buildSearchClearButton } from './components/buildSearchClearButton'
import buildSearchButton from './components/searchButton'
import buildSearchForm, {
  addEventToSearchForm,
  triggerSearch,
} from './components/searchForm'
import buildSearchInput from './components/searchInput'
import { QUERY_PARAM_SEARCH_TERM } from './constants'
// import { getDataAttributeFromSearchElement } from './searchElement'
import {SearchConfig, SearchOptions} from './types/searchOptions'
import initSaasStyle from './utils/saasStyle'

export const buildSearch = async (
  searchElement: HTMLDivElement,
  searchConfig: SearchConfig,
  options?: SearchOptions,
): Promise<void> => {
  // const searchConfig = getDataAttributeFromSearchElement(searchElement)
  //
  // if (!searchConfig) {
  //   return
  // }

  const { callbacks, autoSuggestionDebounceTime = 500 } = options || {}

  const {
    id,
    searchFieldPlaceholderText,
    searchButtonText,
    searchUrl,
    searchTabs,
    loadMoreButtonText,
    autocompleteUrl,
    autocompleteTriggerThreshold,
    autoSuggestText,
    noResultsText,
    trackingUrl,
  } = searchConfig


  const searchContainer = document.createElement('div')
  searchContainer.classList.add('cmp-saas')

  const searchFormElement = buildSearchForm()
  const searchResetButton = buildSearchClearButton()
  const searchInputWrapper = document.createElement('div')
  searchInputWrapper.classList.add('cmp-saas__search-input-wrapper')

  const searchInputElement = buildSearchInput({
    id,
    searchFieldPlaceholderText,
    autocompleteUrl,
    autocompleteTriggerThreshold,
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
  searchInputWrapper.appendChild(searchInputElement)
  searchFormElement.appendChild(searchInputWrapper)
  if (searchButtonElement) {
    searchFormElement.appendChild(searchButtonElement)
  }
  searchResetButton?.classList.add('cmp-saas__search-clear-button--hide')
  searchInputWrapper.appendChild(searchResetButton)

  const searchElementParent = searchElement.parentElement
  searchElementParent?.replaceChild(searchContainer, searchElement)

  searchContainer.appendChild(searchFormElement)

  const currentUrl = new URL(window.location.href)
  const currentParams = new URLSearchParams(currentUrl.search)
  const searchValue = currentParams.get(QUERY_PARAM_SEARCH_TERM)

  if (searchValue) {
    searchInputElement.value = searchValue
    searchResetButton?.classList.remove('cmp-saas__search-clear-button--hide')
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
