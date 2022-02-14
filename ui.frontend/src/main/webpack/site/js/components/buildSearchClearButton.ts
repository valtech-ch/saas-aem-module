import { cleanSessionStorage } from '../utils/sessionStorage'

export const buildSearchClearButton = () => {
  const searchClearButton = document.createElement('span')
  searchClearButton.classList.add('cmp-saas__search-clear-button')

  searchClearButton.addEventListener('click', () => {
    searchClearButton.classList.add('cmp-saas__search-clear-button--hide')
    const searchInput = document.getElementsByClassName(
      'cmp-saas__search-input',
    )?.[0] as HTMLInputElement

    if (searchInput) {
      searchInput.value = ''
      searchInput.focus()
      cleanSessionStorage()
      const suggestionElement = document.getElementById('#cmp-saas-suggestions')
      if (suggestionElement) {
        suggestionElement.remove()
      }
    }
  })

  return searchClearButton
}
