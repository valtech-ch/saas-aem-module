import { buildSearch } from './buildSearch'
import { getSearchElement } from './searchElement'

const initSearch = (): void => {
  window.addEventListener('load', () => {
    const searchElement = getSearchElement()

    if (!searchElement) {
      return
    }

    // eslint-disable-next-line no-void
    void buildSearch(searchElement)
  })
}

export default initSearch
