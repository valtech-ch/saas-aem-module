import { SearchItem } from './searchItem'

type SearchTabOptions = {
  tabId: string
  tabName: string
  tabNumberOfResults: number
  selectTab: (tabId: string) => void
  setResults: () => void
}

export type Tab = {
  tabId: string
  tabName: string
  resultsTotal: number
  results: SearchItem[]
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
  tabNumberOfResults,
  selectTab,
}: SearchTabOptions): HTMLDivElement => {
  const searchTab = document.createElement('div')
  searchTab.classList.add('saas-search-tab')

  const searchTabName = document.createElement('span')
  searchTabName.innerHTML = `${extractTabNameFromUrl(tabName)} `

  const searchTabNumberOfResults = document.createElement('span')
  searchTabNumberOfResults.innerHTML = `(${tabNumberOfResults})`

  searchTab.appendChild(searchTabName)
  searchTab.appendChild(searchTabNumberOfResults)

  searchTab.addEventListener('click', () => {
    selectTab(tabId)
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

export default buildSearchTab
