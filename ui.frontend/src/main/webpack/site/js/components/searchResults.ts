import { OnSearchItemClickCallback } from '../types/callbacks'
import { buildSearchItem, SearchItem } from './searchItem'

type SearchResultsOptions = {
  searchItems: SearchItem[]
  tabId: string
  onSearchItemClick?: OnSearchItemClickCallback
  dataLayer: object
}

export const generateSearchItemList = (
  searchItems: SearchItem[],
  onSearchItemClick?: OnSearchItemClickCallback,
): HTMLDivElement[] => {
  return searchItems.map((searchItem) => {
    const searchItemElement = buildSearchItem(searchItem)

    if (onSearchItemClick) {
      searchItemElement.addEventListener('click', (event) => {
        event.preventDefault()

        onSearchItemClick?.(searchItem.title)
      })
    }
    return searchItemElement
  })
}

const buildSearchResult = ({
  searchItems,
  tabId,
  onSearchItemClick,
  dataLayer,
}: SearchResultsOptions): HTMLDivElement => {
  const searchResults = document.createElement('div')
  searchResults.classList.add('cmp-saas__results')
  searchResults.dataset.cmpDataLayer = JSON.stringify(dataLayer)

  const resultsItems = document.createElement('div')
  resultsItems.classList.add('cmp-saas__results-items')

  searchResults.dataset.tab = tabId

  generateSearchItemList(searchItems, onSearchItemClick).forEach(
    (searchItemElement) => {
      resultsItems.appendChild(searchItemElement)
    },
  )

  searchResults.appendChild(resultsItems)

  return searchResults
}

export default buildSearchResult
