import { createCustomEvent, events } from '../service/serviceEvent'
import { buildSearchItem, SearchItem } from './searchItem'

type SearchResultsOptions = {
  searchItems: SearchItem[]
  tabId: string
}

export const generateSearchItemList = (
  searchItems: SearchItem[],
): HTMLDivElement[] => {
  return searchItems.map((searchItem) => {
    const searchItemElement = buildSearchItem(searchItem)
    if (searchItemElement) {
      searchItemElement.addEventListener('click', (e) => {
        e.target?.dispatchEvent(
          createCustomEvent({
            name: events.searchItem,
            data: {
              title: searchItem.title,
              url: searchItem.url,
              bestBet: searchItem.bestBet,
            },
          }),
        )
      })
    }
    return searchItemElement
  })
}

const buildSearchResult = ({
  searchItems,
  tabId,
}: SearchResultsOptions): HTMLDivElement => {
  const searchResults = document.createElement('div')
  searchResults.classList.add('cmp-saas__results')

  const resultsItems = document.createElement('div')
  resultsItems.classList.add('cmp-saas__results-items')

  searchResults.dataset.tab = tabId

  generateSearchItemList(searchItems).forEach((searchItemElement) => {
    resultsItems.appendChild(searchItemElement)
  })

  searchResults.appendChild(resultsItems)

  return searchResults
}

export default buildSearchResult
