import {
  getDataAttributeFromSearchElement,
  getSearchElement,
} from '../searchElement'

describe('searchElement', () => {
  describe('getSearchElement', () => {
    test('returns null when the element is not on the page', () => {
      document.body.innerHTML = `
            <html lang="en">
                <body>
                    <div></div>
                </body>
            </html>
        `

      expect(getSearchElement()).toBe(null)
    })

    test('returns the search element', () => {
      document.body.innerHTML = `
            <html lang="en">
                <body>
                    <search></search>
                </body>
            </html>
        `

      expect(getSearchElement()).toBeTruthy()
    })
  })

  describe('getDataAttributeFromSearchElement', () => {
    const mockConfig = {
      searchFieldPlaceholderText: 'Type search term here...',
      loadMoreButtonText: 'Load more',
      searchTabs: [
        '/content/saas-aem-module/us/en/_jcr_content/root/container/container/search/search-tabs/searchtab.model.json?q=pg',
      ],
      searchButtonText: 'Search',
      autocompleteTriggerThreshold: 3,
      autosuggestUrl:
        '/content/saas-aem-module/us/en/_jcr_content/root/container/container/search/search-tabs/autosuggest.model.json',
      autoSuggestText: 'Did you mean: ',
    }

    test('returns the correct attributes', () => {
      document.body.innerHTML = `
            <html lang="en">
                <body>
                    <search
                        data-search-config='${JSON.stringify(mockConfig)}'
                        >
                    </search>
                </body>
            </html>
        `

      const searchElement = getSearchElement() as HTMLElement

      const output = getDataAttributeFromSearchElement(searchElement)

      expect(output).toEqual(mockConfig)
    })

    test('returns null if no search element is found', () => {
      document.body.innerHTML = `
            <html lang="en">
                <body>
                </body>
            </html>
        `

      const searchElement = getSearchElement() as HTMLElement

      const output = getDataAttributeFromSearchElement(searchElement)

      expect(output).toEqual(null)
    })

    test.skip('returns null if the search config is invalid', () => {
      document.body.innerHTML = `
            <html lang="en">
                <body>
                    <search
                        data-search-config='{}'
                        >
                    </search>
                </body>
            </html>
        `

      const searchElement = getSearchElement() as HTMLElement

      const output = getDataAttributeFromSearchElement(searchElement)

      expect(output).toEqual(null)
    })

    test.skip('returns null if the search config does not exist', () => {
      document.body.innerHTML = `
            <html lang="en">
                <body>
                    <search
                        data-search-config=''
                        >
                    </search>
                </body>
            </html>
        `

      const searchElement = getSearchElement() as HTMLElement

      const output = getDataAttributeFromSearchElement(searchElement)

      expect(output).toEqual(null)
    })
  })
})
