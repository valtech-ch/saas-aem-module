import { OnSearchItemClickCallback } from '../types/callbacks'
import { buildSearchItem, SearchItem } from './searchItem'

type SearchResultsOptions = {
  searchItems: SearchItem[]
  tabId: string
  onSearchItemClick?: OnSearchItemClickCallback
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
}: SearchResultsOptions): HTMLDivElement => {
  const searchResults = document.createElement('div')
  searchResults.classList.add('saas-container_results')

  searchResults.dataset.tab = tabId

  generateSearchItemList(searchItems, onSearchItemClick).forEach(
    (searchItemElement) => {
      searchResults.appendChild(searchItemElement)
    },
  )

  return searchResults
}

export default buildSearchResult
