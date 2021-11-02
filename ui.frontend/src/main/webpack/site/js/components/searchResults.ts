import { buildSearchItem, SearchItem } from './searchItem'

type SearchResultsOptions = {
  searchItems: SearchItem[]
}

const buildSearchResult = ({
  searchItems,
}: SearchResultsOptions): HTMLDivElement => {
  const searchResults = document.createElement('div')
  searchResults.classList.add('saas-search-results')

  searchItems.forEach((searchItem) => {
    const searchItemElement = buildSearchItem(searchItem)

    searchResults.appendChild(searchItemElement)
  })

  return searchResults
}

export default buildSearchResult
