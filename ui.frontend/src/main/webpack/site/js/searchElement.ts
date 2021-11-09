import type { SearchConfig } from './types/searchOptions'

export const getSearchElement = (
  searchSelector = 'search',
): HTMLElement | null => {
  return document.querySelector(searchSelector)
}

export const isSearchConfig = (
  searchConfig: SearchConfig | never,
): searchConfig is SearchConfig => {
  const {
    searchFieldPlaceholderText,
    loadMoreButtonText,
    searchTabs,
    searchButtonText,
    autocompleteTriggerThreshold,
    searchUrl,
  } = searchConfig

  return (
    typeof searchFieldPlaceholderText === 'string' &&
    Boolean(searchFieldPlaceholderText) &&
    typeof loadMoreButtonText === 'string' &&
    Boolean(loadMoreButtonText) &&
    Array.isArray(searchTabs) &&
    typeof searchButtonText === 'string' &&
    Boolean(searchButtonText) &&
    typeof autocompleteTriggerThreshold === 'number' &&
    (!searchUrl || typeof searchUrl === 'string')
  )
}

export const getDataAttributeFromSearchElement = (
  element: HTMLElement,
): SearchConfig | null => {
  if (!element) {
    return null
  }

  const searchConfigStr = element.dataset.searchConfig || '{}'
  const searchConfig = JSON.parse(searchConfigStr) as SearchConfig

  const isSearchConfigValid = isSearchConfig(searchConfig)

  if (!isSearchConfigValid) {
    return null
  }

  return searchConfig
}
