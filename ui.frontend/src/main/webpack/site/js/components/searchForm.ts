import type { SearchCallbacks } from '../types/callbacks'
import buildLoadMoreButton from './loadMoreButton'
import buildSearchResult from './searchResults'
import buildSearchTab, {
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

  const currentUrl = new URL(window.location.href)
  const currentParams = new URLSearchParams(currentUrl.search)
  currentParams.set('q', searchValue)

  window.history.replaceState(
    {},
    '',
    `${window.location.pathname}?${currentParams.toString()}`,
  )

  removeSearchTabs()
  removeSearchResults()
  removeSelectedTabFromSearchContainer()

  const tabResultsArray = await Promise.all(
    searchTabs.map(async (tab): Promise<Tab> => {
      const tabResults = await fetch(`${tab.url}&q=${searchValue}`)
      const tabResultsJSON = await tabResults.json()

      return { ...tabResultsJSON, tabId: tab.title } as Tab
    }),
  )

  const searchFormParent = searchForm.parentElement

  tabResultsArray.forEach((tabResult) => {
    const { resultsTotal, showLoadMoreButton, tabId, title, results, url } =
      tabResult

    if (resultsTotal) {
      const searchContainer =
        document.querySelector<HTMLDivElement>('.saas-container')

      if (searchContainer && !searchContainer.dataset.selectedTab) {
        searchContainer.dataset.selectedTab = tabResult.tabId
      }

      const searchTabElement = buildSearchTab({
        tabId,
        tabNumberOfResults: resultsTotal,
        title,
        onSwitchTab,
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
  })
}

export const addEventToSearchForm = (
  searchForm: HTMLFormElement,
  searchInputElement: HTMLInputElement,
  searchUrl: string | undefined,
  searchTabs: TabConfig[],
  loadMoreButtonText: string,
  options?: SearchFormSubmitEventOption,
): void => {
  // eslint-disable-next-line @typescript-eslint/no-misused-promises,sonarjs/cognitive-complexity
  return searchForm.addEventListener('submit', (event) => {
    event.preventDefault()

    return triggerSearch(
      searchForm,
      searchInputElement,
      searchUrl,
      searchTabs,
      loadMoreButtonText,
      options,
    )
  })
}

export default buildSearchForm
