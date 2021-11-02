import buildLoadMoreButton from './loadMoreButton'
import buildSearchResult from './searchResults'
import buildSearchTab, { removeSearchTabs, Tab } from './searchTabs'

type SearchFormSubmitEventOption = {
  searchCallback?: () => void
}

const buildSearchForm = (): HTMLFormElement => {
  return document.createElement('form')
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

  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  return searchForm.addEventListener('submit', (event) => {
    event.preventDefault()

    return (async () => {
      searchCallback?.()

      const searchValue = searchInputElement.value

      if (searchUrl) {
        window.location.href = searchUrl
        return
      }

      removeSearchTabs()

      const tabResultsArray = await Promise.all(
        searchTabs.map(async (tab): Promise<Tab> => {
          const tabResults = await fetch(`${tab}&q=${searchValue}`)
          const tabResultsJSON = await tabResults.json()

          return { ...tabResultsJSON, tabId: tab } as Tab
        }),
      )

      const searchFormParent = searchForm.parentElement

      tabResultsArray.forEach((tabResult) => {
        const searchTabElement = buildSearchTab({
          tabId: tabResult.tabId,
          tabName: tabResult.tabId,
          tabNumberOfResults: tabResult.resultsTotal,
          selectTab: () => {},
          setResults: () => {},
        })

        const searchResults = buildSearchResult({
          searchItems: tabResult.results,
        })

        searchFormParent?.appendChild(searchTabElement)
        searchFormParent?.appendChild(searchResults)

        if (tabResult.showLoadMoreButton) {
          const loadMoreButton = buildLoadMoreButton({
            loadMoreButtonText,
            offset: 10,
            tabUrl: tabResult.tabId,
            searchValue,
            searchResultsElement: searchResults,
          })
          searchFormParent?.appendChild(loadMoreButton)
        }
      })
    })()
  })
}

export default buildSearchForm
