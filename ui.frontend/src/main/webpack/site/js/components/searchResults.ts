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

  const resultsItems = document.createElement('div')
  resultsItems.classList.add('saas-container_results_items')

  const resultsPage = document.createElement('div')
  resultsPage.classList.add(
    'saas-container_results_page',
    'saas-container_results_page--1',
  )

  searchResults.dataset.tab = tabId

  generateSearchItemList(searchItems, onSearchItemClick).forEach(
    (searchItemElement) => {
      resultsPage.appendChild(searchItemElement)
    },
  )

  resultsItems.appendChild(resultsPage)
  searchResults.appendChild(resultsItems)

  return searchResults
}

export default buildSearchResult
