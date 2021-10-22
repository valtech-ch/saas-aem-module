type SearchConfig = {
  searchFieldPlaceholderText: string
  loadMoreButtonText: string
  searchTabs: string[]
  searchButtonText: string
  autocompleteTriggerThreshold: number
}

export const getSearchElement = (
  searchSelector = 'search',
): HTMLElement | null => {
  return document.querySelector(searchSelector)
}

export const isSearchConfig = (
  searchConfig: SearchConfig,
): searchConfig is SearchConfig => {
  const {
    searchFieldPlaceholderText,
    loadMoreButtonText,
    searchTabs,
    searchButtonText,
    autocompleteTriggerThreshold,
  } = searchConfig

  return (
    typeof searchFieldPlaceholderText === 'string' &&
    typeof loadMoreButtonText === 'string' &&
    Array.isArray(searchTabs) &&
    typeof searchButtonText === 'string' &&
    typeof autocompleteTriggerThreshold === 'number'
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

  const {
    searchFieldPlaceholderText,
    loadMoreButtonText,
    searchTabs,
    searchButtonText,
    autocompleteTriggerThreshold,
  } = searchConfig

  return {
    searchFieldPlaceholderText,
    loadMoreButtonText,
    searchTabs,
    searchButtonText,
    autocompleteTriggerThreshold,
  }
}
