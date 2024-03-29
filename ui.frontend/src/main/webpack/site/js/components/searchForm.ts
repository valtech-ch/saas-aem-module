import { QUERY_PARAM_SEARCH_TERM } from '../constants'
import { createCustomEvent, events } from '../service/serviceEvent'
import fetchSearch from '../utils/fetchSearch'
import { saveFacetFiltersToAppState } from '../utils/state'
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

const buildSearchForm = (): HTMLFormElement => {
  const searchForm = document.createElement('form')
  searchForm.classList.add('cmp-saas__form')

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
// eslint-disable-next-line sonarjs/cognitive-complexity
): Promise<void> => {
  if (searchInputElement.dataset.loading === 'true') {
    return
  }
  const searchInputElementCopy = searchInputElement

  const hasSearchTabs = searchTabs && searchTabs.length > 0

  if (hasSearchTabs) {
    searchInputElementCopy.dataset.loading = 'true'
  }

  const searchValue = searchInputElement.value

  if (searchUrl && searchUrl != window.location.pathname) {
    const currentUrl = new URL(window.location.href)
    const currentParams = new URLSearchParams(currentUrl.search)
    currentParams.set(QUERY_PARAM_SEARCH_TERM, searchValue)
    window.location.href = `${searchUrl}?${currentParams.toString()}`
  }

  updateUrl(searchValue)

  removeAutosuggest(searchContainer)
  removeSearchTabs(searchContainer)
  removeSearchResults(searchContainer)
  removeSelectedTabFromSearchContainer(searchContainer)

  const tabResultsArray = hasSearchTabs
    ? await Promise.all(
        searchTabs.map(async (tab, index): Promise<Tab> => {
          const tabResultsJSON = await fetchSearch(tab.url, searchValue)

          return { ...tabResultsJSON, tabId: tab.tabId || tab.title, title: tab.title, index } as Tab
        }),
      ).finally(() => {
        searchInputElementCopy.dataset.loading = 'false'
      })
    : null

  const searchFormParent = searchForm.parentElement

  const hasResults = tabResultsArray?.some((tab) => tab.resultsTotal)

  const suggestion = tabResultsArray?.[0]?.suggestion
  const noResultsFound = hasSearchTabs && !hasResults
  const isShowSuggestion = noResultsFound && suggestion
  const isRemoveSaasNotFound = hasSearchTabs && hasResults
  const isDisplayNotFound = noResultsFound && searchFormParent

  if (isRemoveSaasNotFound) {
    searchFormParent
      ?.querySelectorAll('.cmp-saas__not-found')
      ?.forEach((item) => item.remove())
  }

  if (isShowSuggestion) {
    const autoSuggestElement = buildSearchSuggestion(
      suggestion.text,
      autoSuggestText,
    )
    searchFormParent?.append(autoSuggestElement)
  }
  const notFoundElementExists = searchFormParent?.querySelector(
    '.cmp-saas__not-found',
  )
  if (isDisplayNotFound && !notFoundElementExists) {
    const notFoundElement = document.createElement('div')
    notFoundElement.classList.add('cmp-saas__not-found')
    notFoundElement.innerText = noResultsText
    searchFormParent.appendChild(notFoundElement)
  }

  if (noResultsFound) {
    return
  }

  if (tabResultsArray) {
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
        saveFacetFiltersToAppState(tabResult)
        buildSearchResultsTab({
          tabResult,
          searchValue,
          searchForm,
          searchFormParent,
          loadMoreButtonText,
          searchContainer,
        })
      })
  }
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
): void => {
  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  return searchForm.addEventListener('submit', (event) => {
    event.preventDefault()

    event.target?.dispatchEvent(
      createCustomEvent({
        name: events.searchSubmit,
        data: {
          query: searchInputElement.value,
        },
      }),
    )

    return triggerSearch(
      searchForm,
      searchInputElement,
      searchUrl,
      searchTabs,
      loadMoreButtonText,
      autoSuggestText,
      searchContainer,
      noResultsText,
    )
  })
}

export default buildSearchForm
