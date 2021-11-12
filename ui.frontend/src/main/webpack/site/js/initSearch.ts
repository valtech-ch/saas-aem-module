import { buildSearch } from './buildSearch'
import { getSearchElement } from './searchElement'

const initSearch = (): void => {
  window.addEventListener('load', () => {
    const searchElement = getSearchElement()

    if (!searchElement) {
      return
    }

    buildSearch(searchElement)
  })
}

export default initSearch
