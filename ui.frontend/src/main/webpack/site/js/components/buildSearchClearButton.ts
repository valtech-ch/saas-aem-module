import { cleanSessionStorage } from '../utils/sessionStorage'

export const buildSearchClearButton = () => {
  const resetButton = document.createElement('span')
  resetButton.classList.add('cmp-saas__search-clear-button')

  resetButton.addEventListener('click', () => {
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

  return resetButton
}
