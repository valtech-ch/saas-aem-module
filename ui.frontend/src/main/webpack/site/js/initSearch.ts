import { buildSearch } from './buildSearch'
import { getSearchElement } from './searchElement'

exports.default = function initSearch(): void {
  window.addEventListener('load', () => {
    const searchElement = getSearchElement()

    if (!searchElement) {
      return
    }

    void buildSearch(searchElement)
  })
}
