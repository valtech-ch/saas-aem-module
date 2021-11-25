import { CallbackFunction, OnSearchItemClickCallback } from '../types/callbacks'
import buildLoadMoreButton from './loadMoreButton'
import buildSearchResult from './searchResults'
import buildSearchTab, { Tab } from './searchTabs'

type BuildSearchResults = {
  tabResult: Tab
  searchValue: string
  searchForm: HTMLFormElement
  searchFormParent: HTMLElement | null
  loadMoreButtonText: string
  onSearchItemClick?: OnSearchItemClickCallback
  onSwitchTab?: CallbackFunction
  onLoadMoreButtonClick?: CallbackFunction
  searchContainer: HTMLDivElement
}

const buildSearchResultsTab = ({
  tabResult,
  searchValue,
  searchForm,
  searchFormParent,
  loadMoreButtonText,
  onSearchItemClick,
  onSwitchTab,
  onLoadMoreButtonClick,
  searchContainer,
}: BuildSearchResults): void => {
  const { resultsTotal, showLoadMoreButton, tabId, title, results, url } =
    tabResult

  if (resultsTotal) {
    if (searchContainer && tabResult.index === 0) {
      searchContainer.dataset.selectedTab = tabResult.tabId
    }

    const searchTabElement = buildSearchTab({
      tabId,
      tabNumberOfResults: resultsTotal,
      title,
      onSwitchTab,
      searchContainer,
    })

    const searchResults = buildSearchResult({
      searchItems: results,
      tabId,
      onSearchItemClick,
    })

    searchForm?.parentNode?.insertBefore(
      searchTabElement,
      searchForm.nextSibling,
    )
    searchFormParent?.appendChild(searchResults)

    if (showLoadMoreButton) {
      const loadMoreButton = buildLoadMoreButton({
        loadMoreButtonText,
        offset: 1,
        tabUrl: url,
        searchValue,
        searchResultsElement: searchResults,
        onLoadMoreButtonClick,
      })
      searchResults?.appendChild(loadMoreButton)
    }

    if (searchContainer?.dataset.selectedTab !== tabId) {
      searchResults.style.display = 'none'
    }
  }
}

export default buildSearchResultsTab
