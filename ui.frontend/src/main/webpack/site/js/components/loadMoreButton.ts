import { OnLoadMoreButtonClickCallback } from '../types/callbacks'
import fetchSearch from '../utils/fetchSearch'
import { buildSearchItem, SearchItem } from './searchItem'

type SearchButtonOptions = {
  loadMoreButtonText: string
  page: number
  tabUrl: string
  searchValue: string
  searchResultsElement: HTMLDivElement
  onLoadMoreButtonClick?: OnLoadMoreButtonClickCallback
  queryParameterName?: string
}

const buildLoadMoreButton = ({
  loadMoreButtonText,
  page,
  tabUrl,
  searchValue,
  searchResultsElement,
  onLoadMoreButtonClick,
  queryParameterName,
}: SearchButtonOptions): HTMLButtonElement => {
  const loadMoreButton = document.createElement('button')
  loadMoreButton.classList.add('saas-load-more-button')

  loadMoreButton.dataset.page = `${page}`

  loadMoreButton.innerText = loadMoreButtonText

  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  loadMoreButton.addEventListener('click', async (event) => {
    onLoadMoreButtonClick?.()

    event.preventDefault()

    const currentPage = loadMoreButton.dataset.page || page

    const selectedFacets = searchResultsElement?.dataset.facets
      ? JSON.parse(searchResultsElement.dataset.facets)
      : {}

    const resultJSON = await fetchSearch(
      tabUrl,
      searchValue,
      +currentPage,
      queryParameterName,
      selectedFacets,
    )

    loadMoreButton.dataset.page = `${+currentPage + 1}`

    // we must search the results items wrapper from the current 'active' results element.
    const searchResultsItemsWrapper =
      searchResultsElement.getElementsByClassName(
        'saas-container_results_items',
      )?.[0] || undefined
    const resultsPage = document.createElement('div')
    resultsPage.classList.add(
      'saas-container_results_page',
      `saas-container_results_page--${currentPage}`,
    )

    resultJSON?.results.forEach((resultItem: SearchItem) => {
      const searchItemElement = buildSearchItem(resultItem)
      resultsPage.appendChild(searchItemElement)
    })

    if (searchResultsItemsWrapper) {
      searchResultsItemsWrapper.appendChild(resultsPage)
      searchResultsElement.insertBefore(
        searchResultsItemsWrapper,
        loadMoreButton,
      )
    }

    if (!resultJSON?.showLoadMoreButton) {
      loadMoreButton.remove()
    }
  })

  return loadMoreButton
}

export default buildLoadMoreButton
