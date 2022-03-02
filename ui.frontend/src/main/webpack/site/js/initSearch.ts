import { buildSearch } from './buildSearch'
import { getSearchElements } from './searchElement'
import { cleanSessionStorage } from './utils/sessionStorage'

function initSearch(): void {
  window.addEventListener('load', () => {
    const searchElements = getSearchElements()
    cleanSessionStorage()

    if (!searchElements) {
      return
    }

    searchElements?.forEach((searchElement) => {
      void buildSearch(searchElement as HTMLDivElement)
    })
  })
}

export default initSearch
