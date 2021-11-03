import buildLoadMoreButton from './loadMoreButton'
import buildSearchResult from './searchResults'
import buildSearchTab, {
  removeSearchResults,
  removeSearchTabs,
  removeSelectedTabFromSearchContainer,
  Tab,
} from './searchTabs'

type SearchFormSubmitEventOption = {
  searchCallback?: () => void
}

const buildSearchForm = (): HTMLFormElement => {
  const searchForm = document.createElement('form')
  searchForm.classList.add('saas-search-form')

  return searchForm
}

export const addEventToSearchForm = (
  searchForm: HTMLFormElement,
  searchInputElement: HTMLInputElement,
  searchUrl: string | undefined,
  searchTabs: string[],
  loadMoreButtonText: string,
  options?: SearchFormSubmitEventOption,
): void => {
  const { searchCallback } = options || {}

  // eslint-disable-next-line @typescript-eslint/no-misused-promises,sonarjs/cognitive-complexity
  return searchForm.addEventListener('submit', (event) => {
    event.preventDefault()

    return (async () => {
      searchCallback?.()

      if (searchUrl) {
        window.location.href = searchUrl
        return
      }

      removeSearchTabs()
      removeSearchResults()
      removeSelectedTabFromSearchContainer()

      const searchValue = searchInputElement.value

      const tabResultsArray = await Promise.all(
        searchTabs.map(async (tab): Promise<Tab> => {
          const tabResults = await fetch(`${tab}&q=${searchValue}`)
          const tabResultsJSON = await tabResults.json()

          return { ...tabResultsJSON, tabId: tab } as Tab
        }),
      )

      const searchFormParent = searchForm.parentElement

      tabResultsArray.forEach((tabResult) => {
        const { resultsTotal, showLoadMoreButton, tabId } = tabResult

        if (resultsTotal) {
          const searchContainer = document.querySelector<HTMLDivElement>(
            '.saas-search-container',
          )

          if (searchContainer && !searchContainer.dataset.selectedTab) {
            searchContainer.dataset.selectedTab = tabResult.tabId
          }

          const searchTabElement = buildSearchTab({
            tabId: tabResult.tabId,
            tabName: tabResult.tabId,
            tabNumberOfResults: tabResult.resultsTotal,
            title: tabResult.title,
          })

          const searchResults = buildSearchResult({
            searchItems: tabResult.results,
            tabId: tabResult.tabId,
          })

          searchForm?.parentNode?.insertBefore(
            searchTabElement,
            searchForm.nextSibling,
          )
          searchFormParent?.appendChild(searchResults)

          if (showLoadMoreButton) {
            const loadMoreButton = buildLoadMoreButton({
              loadMoreButtonText,
              offset: 10,
              tabUrl: tabId,
              searchValue,
              searchResultsElement: searchResults,
            })
            searchResults?.appendChild(loadMoreButton)
          }

          if (searchContainer?.dataset.selectedTab !== tabResult.tabId) {
            searchResults.style.display = 'none'
          }
        }
      })
    })()
  })
}

export default buildSearchForm
