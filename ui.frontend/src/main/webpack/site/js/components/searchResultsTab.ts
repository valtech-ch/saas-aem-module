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
  searchContainer: HTMLDivElement
}

const CMP_SAAS_RESULTS_CLASS = 'cmp-saas__results'
const CMP_SAAS_RESULTS_SHOW_CLASS = `${CMP_SAAS_RESULTS_CLASS}--show`
const CMP_SAAS_RESULTS_HIDE_CLASS = `${CMP_SAAS_RESULTS_CLASS}--hide`

const buildSearchResultsTab = ({
  tabResult,
  searchValue,
  searchForm,
  searchFormParent,
  loadMoreButtonText,

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
      searchContainer,
    })

    const searchResults = buildSearchResult({
      searchItems: results,
      tabId,
    })

    searchForm?.parentNode?.insertBefore(
      searchTabElement,
      searchForm.nextSibling,
    )

    const facetsGroups = document.createElement('div')
    facetsGroups.classList.add('cmp-sass__facets-groups')

    facetFilters?.items.forEach((facetFilter) => {
      const facetsGroup = buildFacetsGroup({
        filterFieldLabel: facetFilter.filterFieldLabel,
        filterFieldName: facetFilter.filterFieldName,
        filterFieldOptions: facetFilter.filterFieldOptions,
        tabUrl: url,
        searchValue,
        queryParameterName: facetFilters.queryParameterName,
        tabId,
        loadMoreButtonText,
        title,
      })

      facetsGroups?.appendChild(facetsGroup)
    })

    if (facetFilters) {
      searchResults.prepend(facetsGroups)
    }

    searchFormParent?.appendChild(searchResults)

    if (showLoadMoreButton) {
      const loadMoreButton = buildLoadMoreButton({
        loadMoreButtonText,
        page: 2,
        tabUrl: url,
        searchValue,
        searchResultsElement: searchResults,
      })
      searchResults?.appendChild(loadMoreButton)
    }

    if (searchContainer?.dataset.selectedTab !== tabId) {
      searchResults.classList.remove(CMP_SAAS_RESULTS_SHOW_CLASS)
      searchResults.classList.add(CMP_SAAS_RESULTS_HIDE_CLASS)
      searchResults.dataset.selected = 'false'
      return
    }

    searchResults.dataset.selected = 'true'
    searchResults.classList.remove(CMP_SAAS_RESULTS_HIDE_CLASS)
    searchResults.classList.add(CMP_SAAS_RESULTS_SHOW_CLASS)
  }
}

export default buildSearchResultsTab
