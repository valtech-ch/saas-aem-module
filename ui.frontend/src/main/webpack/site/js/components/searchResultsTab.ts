import { CallbackFunction, OnSearchItemClickCallback } from '../types/callbacks'
import buildFacetsGroup from './buildFacetsGroup'
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
  const {
    resultsTotal,
    showLoadMoreButton,
    tabId,
    title,
    results,
    url,
    facetFilters,
  } = tabResult

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

    const facetsGroups = document.createElement('div')
    facetsGroups.classList.add('saas-facets-groups')

    facetFilters?.items.forEach((facetFilter) => {
      const facetsGroup = buildFacetsGroup({
        filterFieldLabel: facetFilter.filterFieldLabel,
        filterFieldOptions: facetFilter.filterFieldOptions,
        filterFieldName: facetFilter.filterFieldName,
        tabUrl: url,
        searchValue,
        queryParameterName: facetFilters.queryParameterName,
        tabId,
        onSearchItemClick,
        loadMoreButtonText,
      })

      facetsGroups?.appendChild(facetsGroup)
    })

    searchResults.prepend(facetsGroups)
    searchFormParent?.appendChild(searchResults)

    if (showLoadMoreButton) {
      const loadMoreButton = buildLoadMoreButton({
        loadMoreButtonText,
        offset: 2,
        tabUrl: url,
        searchValue,
        searchResultsElement: searchResults,
        onLoadMoreButtonClick,
      })
      searchResults?.appendChild(loadMoreButton)
    }

    searchResults.dataset.selected = 'true'
    if (searchContainer?.dataset.selectedTab !== tabId) {
      searchResults.style.display = 'none'
      searchResults.dataset.selected = 'false'
    }
  }
}

export default buildSearchResultsTab
