import { TabConfig } from '../components/searchTabs'
import { SearchCallbacks } from './callbacks'

export type SearchOptions = {
  callbacks: SearchCallbacks
}

export type SearchConfig = {
  searchFieldPlaceholderText: string
  loadMoreButtonText: string
  searchTabs: TabConfig[]
  searchButtonText: string
  autocompleteTriggerThreshold: number
  searchUrl?: string
}
