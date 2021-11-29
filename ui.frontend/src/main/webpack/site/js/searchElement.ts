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
    autocompleteTriggerThreshold,
    autocompleteUrl,
    searchUrl,
  } = searchConfig

  return (
    typeof searchFieldPlaceholderText === 'string' &&
    Boolean(searchFieldPlaceholderText) &&
    typeof autocompleteTriggerThreshold === 'number' &&
    (!searchUrl || typeof searchUrl === 'string') &&
    typeof autocompleteUrl === 'string' &&
    Boolean(autocompleteUrl)
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
    // eslint-disable-next-line no-console
    console.warn('SAAS: Invalid search config', searchConfig)

    return null
  }

  return searchConfig
}
