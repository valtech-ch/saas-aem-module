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
  options?: SearchFormSubmitEventOption,
): Promise<void> => {
  const searchValue = searchInputElement.value
  const { onSearch, onSwitchTab, onSearchItemClick, onLoadMoreButtonClick } =
    options || {}

  onSearch?.(searchValue)

  if (searchUrl) {
    window.location.href = searchUrl
    return
  }

  updateUrl(searchValue)

  removeAutosuggest()
  removeSearchTabs()
  removeSearchResults()
  removeSelectedTabFromSearchContainer()

  const tabResultsArray = await Promise.all(
    searchTabs.map(async (tab, index): Promise<Tab> => {
      const tabResultsJSON = await fetchSearch(tab.url, searchValue)

      return { ...tabResultsJSON, tabId: tab.title, index } as Tab
    }),
  )

  const searchFormParent = searchForm.parentElement

  const hasResults = tabResultsArray.some((tab) => tab.resultsTotal)

  if (!hasResults && tabResultsArray?.[0]?.suggestion) {
    const autoSuggestElement = buildSearchSuggestion(
      tabResultsArray[0].suggestion.text,
      autoSuggestText,
    )

    searchFormParent?.append(autoSuggestElement)

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
      options,
    )
  })
}

export default buildSearchForm
