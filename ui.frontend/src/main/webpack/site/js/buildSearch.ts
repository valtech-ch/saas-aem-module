import buildSearchButton from './components/searchButton'
import buildSearchForm, { addEventToSearchForm } from './components/searchForm'
import buildSearchInput from './components/searchInput'
import { getDataAttributeFromSearchElement } from './searchElement'
import { SearchOptions } from './types/searchOptions'

export const buildSearch = (
  searchElement: HTMLElement,
  options?: SearchOptions,
): void => {
  const searchConfig = getDataAttributeFromSearchElement(searchElement)

  if (!searchConfig) {
    return
  }

  const { callbacks } = options || {}
  const { onSearch } = callbacks || {}

  const {
    searchFieldPlaceholderText,
    searchButtonText,
    searchUrl,
    searchTabs,
    loadMoreButtonText,
  } = searchConfig

  const searchContainer = document.createElement('div')
  searchContainer.classList.add('saas-search-container')

  const searchFormElement = buildSearchForm()

  const searchInputElement = buildSearchInput({
    searchFieldPlaceholderText,
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
    { onSearch },
  )

  searchFormElement.appendChild(searchInputElement)
  searchFormElement.appendChild(searchButtonElement)

  const searchElementParent = searchElement.parentElement
  searchElementParent?.replaceChild(searchContainer, searchElement)

  searchContainer.appendChild(searchFormElement)
}
