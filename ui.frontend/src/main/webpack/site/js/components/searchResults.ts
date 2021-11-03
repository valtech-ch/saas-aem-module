import { buildSearchItem, SearchItem } from './searchItem'

type SearchResultsOptions = {
  searchItems: SearchItem[]
  tabId: string
}

const buildSearchResult = ({
  searchItems,
  tabId,
}: SearchResultsOptions): HTMLDivElement => {
  const searchResults = document.createElement('div')
  searchResults.classList.add('saas-search-results')

  searchResults.dataset.tab = tabId

  searchItems.forEach((searchItem) => {
    const searchItemElement = buildSearchItem(searchItem)

    searchResults.appendChild(searchItemElement)
  })

  return searchResults
}

export default buildSearchResult
