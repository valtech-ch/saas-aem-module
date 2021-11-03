import { buildSearchItem, SearchItem } from './searchItem'

type SearchButtonOptions = {
  loadMoreButtonText: string
  offset: number
  tabUrl: string
  searchValue: string
  searchResultsElement: HTMLDivElement
}

const buildLoadMoreButton = ({
  loadMoreButtonText,
  offset,
  tabUrl,
  searchValue,
  searchResultsElement,
}: SearchButtonOptions): HTMLButtonElement => {
  const loadMoreButton = document.createElement('button')
  loadMoreButton.classList.add('saas-load-more-button')

  loadMoreButton.dataset.offset = `${offset}`

  loadMoreButton.innerText = loadMoreButtonText

  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  loadMoreButton.addEventListener('click', async (event) => {
    event.preventDefault()

    const currentOffset = loadMoreButton.dataset.offset || offset

    const result = await fetch(
      `${tabUrl}&q=${searchValue}&start=${currentOffset || 0}`,
    )

    loadMoreButton.dataset.offset = `${+currentOffset + offset}`

    const resultJSON = await result.json()

    resultJSON.results.forEach((resultItem: SearchItem) => {
      const searchItemElement = buildSearchItem(resultItem)

      // searchResultsElement.appendChild(searchItemElement)
      searchResultsElement.insertBefore(searchItemElement, loadMoreButton)
    })

    if (!resultJSON.showLoadMoreButton) {
      loadMoreButton.remove()
    }
  })

  return loadMoreButton
}

export default buildLoadMoreButton
