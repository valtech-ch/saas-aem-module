import { OnSwitchTabCallback } from '../types/callbacks'
import { SearchItem } from './searchItem'

export type SearchTabOptions = {
  tabId: string
  tabName: string
  tabNumberOfResults: number
  title: string
  onSwitchTab?: OnSwitchTabCallback
}

export type Tab = {
  tabId: string
  tabName: string
  resultsTotal: number
  results: SearchItem[]
  title: string
  showLoadMoreButton: boolean
}

const extractTabNameFromUrl = (url: string) => {
  const tabNameCaptureRegex = new RegExp('search-tabs/(.+).model.json')

  const captureGroup = tabNameCaptureRegex.exec(url)

  return captureGroup?.length === 2 ? captureGroup[1] : url
}

const buildSearchTab = ({
  tabId,
  tabName,
  title,
  tabNumberOfResults,
  onSwitchTab,
}: SearchTabOptions): HTMLDivElement => {
  const searchTab = document.createElement('div')
  searchTab.classList.add('saas-search-tab')

  const searchTabName = document.createElement('span')
  searchTabName.innerHTML = title || `${extractTabNameFromUrl(tabName)} `

  const searchTabNumberOfResults = document.createElement('span')
  searchTabNumberOfResults.innerHTML = `(${tabNumberOfResults})`

  searchTab.appendChild(searchTabName)
  searchTab.appendChild(searchTabNumberOfResults)

  searchTab.addEventListener('click', () => {
    onSwitchTab?.()

    const searchTabs = document.querySelectorAll<HTMLDivElement>(
      '.saas-search-results',
    )
    const searchContainer = document.querySelector<HTMLDivElement>(
      '.saas-search-container',
    )

    if (searchContainer) {
      searchContainer.dataset.selectedTab = tabId
    }

    searchTabs?.forEach((tab) => {
      const tabElement = tab

      if (tabElement.dataset.tab === tabId) {
        tabElement.style.display = 'block'
      } else {
        tabElement.style.display = 'none'
      }
    })
  })

  return searchTab
}

export const removeSearchTabs = (): void => {
  const searchTabs =
    document.querySelectorAll<HTMLDivElement>('.saas-search-tab')

  searchTabs.forEach((tab) => {
    tab.remove()
  })
}

export const removeSearchResults = (): void => {
  const searchResults = document.querySelectorAll<HTMLDivElement>(
    '.saas-search-results',
  )

  searchResults.forEach((searchResult) => {
    searchResult.remove()
  })
}

export const removeSelectedTabFromSearchContainer = (): void => {
  const searchContainer = document.querySelector<HTMLDivElement>(
    '.saas-search-container',
  )

  if (searchContainer) {
    searchContainer.removeAttribute('data-selected-tab')
  }
}

export default buildSearchTab
