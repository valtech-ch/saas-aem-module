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
    autocompleteUrl,
    searchUrl,
    autoSuggestText,
    noResultsText,
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
    (!searchUrl || typeof searchUrl === 'string') &&
    typeof autocompleteUrl === 'string' &&
    Boolean(autocompleteUrl) &&
    typeof autoSuggestText === 'string' &&
    Boolean(autoSuggestText) &&
    typeof noResultsText === 'string' &&
    Boolean(noResultsText)
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
