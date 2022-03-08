import type { SearchConfig } from './types/searchOptions'

export const getSearchElement = (
  searchSelector = 'search',
): HTMLElement | null => {
  return document.querySelector(searchSelector)
}

export const getSearchElements = (
  searchSelector = 'search',
): NodeListOf<HTMLElement> | null => {
  return document.querySelectorAll(searchSelector)
}

export const isSearchConfig = (
  searchConfig: SearchConfig | never,
): searchConfig is SearchConfig => {
  const { id, searchFieldPlaceholderText, searchUrl } = searchConfig

  return (
    typeof id === 'string' &&
    Boolean(id) &&
    typeof searchFieldPlaceholderText === 'string' &&
    Boolean(searchFieldPlaceholderText) &&
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
    // eslint-disable-next-line no-console
    console.warn('SAAS: Invalid search config', searchConfig)

    return null
  }

  return searchConfig
}
