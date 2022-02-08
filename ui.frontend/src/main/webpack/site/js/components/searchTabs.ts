import { OnSwitchTabCallback } from '../types/callbacks'
import { FacetFilters } from '../types/facetFilter'
import { SearchItem } from './searchItem'

export type SearchTabOptions = {
  tabId: string
  tabNumberOfResults: number
  title: string
  onSwitchTab?: OnSwitchTabCallback
  searchContainer: HTMLDivElement
}

export type TabConfig = {
  title: string
  url: string
}

export type Suggestion = {
  hits: number
  text: string
}

export type Tab = {
  tabId: string
  tabName: string
  index: number
  resultsTotal: number
  results: SearchItem[]
  title: string
  showLoadMoreButton?: boolean
  url: string
  suggestion?: Suggestion
  facetFilters?: FacetFilters
}

const buildSearchTab = ({
  tabId,
  title,
  tabNumberOfResults,
  onSwitchTab,
  searchContainer,
}: SearchTabOptions): HTMLButtonElement => {
  const searchTab = document.createElement('button')
  searchTab.classList.add('saas-container__tab')

  const searchTabName = document.createElement('span')
  searchTabName.innerHTML = title

  const searchTabNumberOfResults = document.createElement('span')
  searchTabNumberOfResults.innerHTML = ` (${tabNumberOfResults})`

  searchTab.appendChild(searchTabName)
  searchTab.appendChild(searchTabNumberOfResults)

  searchTab.addEventListener('click', () => {
    onSwitchTab?.()

    const searchTabs = searchContainer.querySelectorAll<HTMLDivElement>(
      '.saas-container__results',
    )

    searchContainer.dataset.selectedTab = tabId

    searchTabs?.forEach((tab) => {
      const tabElement = tab

      if (tabElement.dataset.tab === tabId) {
        tabElement.style.display = 'block'
        return
      }

      // overwrite display: grid !important set on .saas-container__results
      tabElement.style.setProperty('display', 'none', 'important')
    })
  })

  return searchTab
}

export const removeAutosuggest = (searchContainer: HTMLDivElement): void => {
  const autoSuggestElement =
    searchContainer.querySelector<HTMLDivElement>('.saas-autosuggest')

  autoSuggestElement?.remove()
}

export const removeSearchTabs = (searchContainer: HTMLDivElement): void => {
  const searchTabs = searchContainer.querySelectorAll<HTMLDivElement>(
    '.saas-container__tab',
  )

  searchTabs.forEach((tab) => {
    tab.remove()
  })
}

export const removeSearchResults = (searchContainer: HTMLDivElement): void => {
  const searchResults = searchContainer.querySelectorAll<HTMLDivElement>(
    '.saas-container__results',
  )

  searchResults.forEach((searchResult) => {
    searchResult.remove()
  })
}

export const removeSelectedTabFromSearchContainer = (
  searchContainer: HTMLDivElement,
): void => {
  searchContainer.removeAttribute('data-selected-tab')
}

export default buildSearchTab
