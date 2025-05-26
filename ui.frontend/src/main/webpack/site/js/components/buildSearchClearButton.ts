import { cleanSessionStorage } from '../utils/sessionStorage'
export const buildSearchClearButton = ({
  searchContainer
}: {
  searchContainer: HTMLDivElement
}) => {
  const searchClearButton = document.createElement('span')
  searchClearButton.classList.add('cmp-saas__search-clear-button')

  searchClearButton.addEventListener('click', () => {
    searchClearButton.classList.add('cmp-saas__search-clear-button--hide')
    const searchInput = searchContainer.getElementsByClassName('cmp-saas__search-input',
    )?.[0] as HTMLInputElement

    if (searchInput) {
      searchInput.value = ''
      searchInput.focus()
      cleanSessionStorage()
      const suggestionElement = searchContainer.querySelector('#cmp-saas-suggestions')
      if (suggestionElement) {
        suggestionElement.remove()
      }
    }
  })

  return searchClearButton
}
