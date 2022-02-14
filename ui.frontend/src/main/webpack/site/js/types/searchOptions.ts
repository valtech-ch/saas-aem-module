import {TabConfig} from '../components/searchTabs'
import {SearchCallbacks} from './callbacks'

export type SearchOptions = {
  callbacks: SearchCallbacks
  autoSuggestionDebounceTime: number
}

export type SearchConfig = {
  id: string
  searchFieldPlaceholderText: string
  loadMoreButtonText: string
  searchTabs: TabConfig[]
  searchButtonText: string
  autoCompleteTriggerThreshold: number
  searchUrl?: string
  autoCompleteUrl: string
  autoSuggestText: string
  noResultsText: string
}
