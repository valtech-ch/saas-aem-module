import { TabConfig } from '../components/searchTabs'

export type SearchOptions = {
  autoSuggestionDebounceTime: number
}

export type SearchConfig = {
  id: string
  searchFieldPlaceholderText: string
  loadMoreButtonText: string
  searchTabs: TabConfig[]
  searchButtonText: string
  autocompleteTriggerThreshold: number
  searchUrl?: string
  autocompleteUrl: string
  autoSuggestText: string
  noResultsText: string
  trackingUrl?: string
}
