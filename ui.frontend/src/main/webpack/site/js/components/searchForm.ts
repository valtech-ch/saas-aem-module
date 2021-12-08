import type { SearchCallbacks } from '../types/callbacks'
import fetchSearch from '../utils/fetchSearch'
import updateUrl from '../utils/updateUrl'
import buildSearchResultsTab from './searchResultsTab'
import buildSearchSuggestion from './searchSuggestion'
import {
  removeAutosuggest,
  removeSearchResults,
  removeSearchTabs,
  removeSelectedTabFromSearchContainer,
  Tab,
  TabConfig,
} from './searchTabs'

type SearchFormSubmitEventOption = SearchCallbacks

const buildSearchForm = (): HTMLFormElement => {
  const searchForm = document.createElement('form')
  searchForm.classList.add('saas-container_form')

  return searchForm
}

export const triggerSearch = async (
  searchForm: HTMLFormElement,
  searchInputElement: HTMLInputElement,
  searchUrl: string | undefined,
  searchTabs: TabConfig[],
  loadMoreButtonText: string,
  autoSuggestText: string,
  searchContainer: HTMLDivElement,
  noResultsText: string,
  options?: SearchFormSubmitEventOption,
): Promise<void> => {
  if (searchInputElement.dataset.loading === 'true') {
    return
  }
  const searchInputElementCopy = searchInputElement

  searchInputElementCopy.dataset.loading = 'true'

  const searchValue = searchInputElement.value
  const { onSearch, onSwitchTab, onSearchItemClick, onLoadMoreButtonClick } =
    options || {}

  onSearch?.(searchValue)

  if (searchUrl) {
    window.location.href = searchUrl
    return
  }

  updateUrl(searchValue)

  removeAutosuggest(searchContainer)
  removeSearchTabs(searchContainer)
  removeSearchResults(searchContainer)
  removeSelectedTabFromSearchContainer(searchContainer)

  const tabResultsArray = await Promise.all(
    searchTabs.map(async (tab, index): Promise<Tab> => {
      const tabResultsJSON = await fetchSearch(tab.url, searchValue)

      return { ...tabResultsJSON, tabId: tab.title, index } as Tab
    }),
  ).finally(() => {
    searchInputElementCopy.dataset.loading = 'false'
  })

  const searchFormParent = searchForm.parentElement

  const hasResults = tabResultsArray.some((tab) => tab.resultsTotal)

  if (!hasResults) {
    if (tabResultsArray?.[0]?.suggestion) {
      const autoSuggestElement = buildSearchSuggestion(
        tabResultsArray[0].suggestion.text,
        autoSuggestText,
      )

      searchFormParent?.append(autoSuggestElement)
    }

    const notFoundElement = document.createElement('div')
    notFoundElement.classList.add('saas-not-found')
    notFoundElement.innerText = noResultsText
    searchFormParent?.appendChild(notFoundElement)

    return
  }

  tabResultsArray
    .sort((tab1, tab2) => {
      if (tab1.index < tab2.index) {
        return 1
      }

      if (tab2.index < tab1.index) {
        return -1
      }

      return 0
    })
    .forEach((tabResult) => {
      buildSearchResultsTab({
        tabResult,
        searchValue,
        searchForm,
        searchFormParent,
        loadMoreButtonText,
        onSearchItemClick,
        onSwitchTab,
        onLoadMoreButtonClick,
        searchContainer,
      })
    })
}

export const addEventToSearchForm = (
  searchForm: HTMLFormElement,
  searchInputElement: HTMLInputElement,
  searchUrl: string | undefined,
  searchTabs: TabConfig[],
  loadMoreButtonText: string,
  autoSuggestText: string,
  searchContainer: HTMLDivElement,
  noResultsText: string,
  options?: SearchFormSubmitEventOption,
): void => {
  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  return searchForm.addEventListener('submit', (event) => {
    event.preventDefault()

    return triggerSearch(
      searchForm,
      searchInputElement,
      searchUrl,
      searchTabs,
      loadMoreButtonText,
      autoSuggestText,
      searchContainer,
      noResultsText,
      options,
    )
  })
}

export default buildSearchForm
