import buildSearchButton from './components/searchButton'
import buildSearchForm, {
  addEventToSearchForm,
  triggerSearch,
} from './components/searchForm'
import buildSearchInput from './components/searchInput'
import { getDataAttributeFromSearchElement } from './searchElement'
import { SearchOptions } from './types/searchOptions'

export const buildSearch = async (
  searchElement: HTMLElement,
  options?: SearchOptions,
): Promise<void> => {
  const searchConfig = getDataAttributeFromSearchElement(searchElement)

  if (!searchConfig) {
    return
  }

  const { callbacks, autoSuggestionDebounceTime = 500 } = options || {}

  const {
    searchFieldPlaceholderText,
    searchButtonText,
    searchUrl,
    searchTabs,
    loadMoreButtonText,
    autosuggestUrl,
    autocompleteTriggerThreshold,
  } = searchConfig

  const searchContainer = document.createElement('div')
  searchContainer.classList.add('saas-container')

  const searchFormElement = buildSearchForm()

  const searchInputElement = buildSearchInput({
    searchFieldPlaceholderText,
    autosuggestUrl,
    autocompleteTriggerThreshold,
    autoSuggestionDebounceTime,
  })

  const searchButtonElement = buildSearchButton({
    searchButtonText,
  })

  addEventToSearchForm(
    searchFormElement,
    searchInputElement,
    searchUrl,
    searchTabs,
    loadMoreButtonText,
    callbacks,
  )

  searchFormElement.appendChild(searchInputElement)
  searchFormElement.appendChild(searchButtonElement)

  const searchElementParent = searchElement.parentElement
  searchElementParent?.replaceChild(searchContainer, searchElement)

  searchContainer.appendChild(searchFormElement)

  const currentUrl = new URL(window.location.href)
  const currentParams = new URLSearchParams(currentUrl.search)
  const searchValue = currentParams.get('q')

  if (searchValue) {
    searchInputElement.value = searchValue

    await triggerSearch(
      searchFormElement,
      searchInputElement,
      searchUrl,
      searchTabs,
      loadMoreButtonText,
      callbacks,
    )
  }
}
