import { buildSearch } from './buildSearch'
import { getSearchElements } from './searchElement'

function initSearch(): void {
  window.addEventListener('load', () => {
    const searchElements = getSearchElements()

    if (!searchElements) {
      return
    }

    searchElements?.forEach((searchElement) => {
      void buildSearch(searchElement as HTMLDivElement)
    })
  })
}

export default initSearch
