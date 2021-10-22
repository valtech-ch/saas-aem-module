import buildSearchButton from './components/searchButton'
import buildSearchInput from './components/searchInput'
import { getDataAttributeFromSearchElement } from './searchElement'

export const buildSearch = (searchElement: HTMLElement): void => {
  const searchConfig = getDataAttributeFromSearchElement(searchElement)

  if (!searchConfig) {
    return
  }

  const { searchFieldPlaceholderText, searchButtonText } = searchConfig

  const searchContainer = document.createElement('div')
  const searchFormElement = document.createElement('form')

  const searchInputElement = buildSearchInput({
    searchFieldPlaceholderText,
  })

  const searchButtonElement = buildSearchButton({
    searchButtonText,
  })

  searchFormElement.appendChild(searchInputElement)
  searchFormElement.appendChild(searchButtonElement)

  searchFormElement.addEventListener('submit', (event) => {
    event.preventDefault()

    const searchValue = searchInputElement.value

    console.log({ searchValue })
  })

  const searchElementParent = searchElement.parentElement
  searchElementParent?.replaceChild(searchContainer, searchElement)

  searchContainer.appendChild(searchFormElement)
}
