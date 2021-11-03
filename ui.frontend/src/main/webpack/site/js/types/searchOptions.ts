import { SearchCallbacks } from './callbacks'

export type SearchOptions = {
  callbacks: SearchCallbacks
}

export type SearchConfig = {
  searchFieldPlaceholderText: string
  loadMoreButtonText: string
  searchTabs: string[]
  searchButtonText: string
  autocompleteTriggerThreshold: number
  searchUrl?: string
}
