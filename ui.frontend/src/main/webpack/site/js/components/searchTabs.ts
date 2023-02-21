import { createCustomEvent, events } from '../service/serviceEvent'
import { FacetFilters } from '../types/facetFilter'
import { SearchItem } from './searchItem'

export type SearchTabOptions = {
  tabId: string
  tabNumberOfResults: number
  title: string
  searchContainer: HTMLDivElement
}

export type TabConfig = {
  title: string
  url: string
  tabId: string
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

const CMP_SAAS_RESULTS_CLASS = 'cmp-saas__results'
const CMP_SAAS_RESULTS_SHOW_CLASS = `${CMP_SAAS_RESULTS_CLASS}--show`
const CMP_SAAS_RESULTS_HIDE_CLASS = `${CMP_SAAS_RESULTS_CLASS}--hide`

const buildSearchTab = ({
  tabId,
  title,
  tabNumberOfResults,

  searchContainer,
}: SearchTabOptions): HTMLButtonElement => {
  const searchTab = document.createElement('button')
  searchTab.classList.add('cmp-saas__tab')

  const searchTabName = document.createElement('span')
  searchTabName.innerHTML = title

  const searchTabNumberOfResults = document.createElement('span')
  searchTabNumberOfResults.innerHTML = ` (${tabNumberOfResults})`

  searchTab.appendChild(searchTabName)
  searchTab.appendChild(searchTabNumberOfResults)

  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  searchTab.addEventListener('click', (e) => {
    const searchTabs =
      searchContainer.querySelectorAll<HTMLDivElement>('.cmp-saas__results')

    searchContainer.dataset.selectedTab = tabId
    window.location.hash = tabId


    searchTabs?.forEach((tab) => {
      const tabElement = tab

      if (tabElement.dataset.tab === tabId) {
        tabElement.classList.add(CMP_SAAS_RESULTS_SHOW_CLASS)
        tabElement.classList.remove(CMP_SAAS_RESULTS_HIDE_CLASS)
        tabElement.dataset.selected = 'true'

        return
      }

      tabElement.classList.remove(CMP_SAAS_RESULTS_SHOW_CLASS)
      tabElement.classList.add(CMP_SAAS_RESULTS_HIDE_CLASS)
      tabElement.dataset.selected = 'false'
      
      e.target?.dispatchEvent(
        createCustomEvent({
          name: events.tabswitch,
          data: { tabId, title, tabNumberOfResults },
        }),
      )
    })
  })

  return searchTab
}

export const removeAutosuggest = (searchContainer: HTMLDivElement): void => {
  const autoSuggestElement = searchContainer.querySelector<HTMLDivElement>(
    '.cmp-saas__autosuggest',
  )

  autoSuggestElement?.remove()
}

export const removeSearchTabs = (searchContainer: HTMLDivElement): void => {
  const searchTabs =
    searchContainer.querySelectorAll<HTMLDivElement>('.cmp-saas__tab')

  searchTabs.forEach((tab) => {
    tab.remove()
  })
}

export const removeSearchResults = (searchContainer: HTMLDivElement): void => {
  const searchResults =
    searchContainer.querySelectorAll<HTMLDivElement>('.cmp-saas__results')

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
