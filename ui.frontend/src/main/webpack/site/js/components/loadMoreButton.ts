import { createCustomEvent, events } from '../service/serviceEvent'
import fetchSearch, { FilterSearchParams } from '../utils/fetchSearch'
import { buildSearchItem, SearchItem } from './searchItem'

type SearchButtonOptions = {
  loadMoreButtonText: string
  page: number
  tabUrl: string
  searchValue: string
  searchResultsElement: HTMLDivElement
  queryParameterName?: string
}

const buildLoadMoreButton = ({
  loadMoreButtonText,
  page,
  tabUrl,
  searchValue,
  searchResultsElement,
  queryParameterName,
}: SearchButtonOptions): HTMLButtonElement => {
  const loadMoreButton = document.createElement('button')
  loadMoreButton.classList.add('cmp-saas__load-more-button')

  loadMoreButton.dataset.page = `${page}`

  loadMoreButton.innerText = loadMoreButtonText

  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  loadMoreButton.addEventListener('click', async (e) => {
    const currentPage = loadMoreButton.dataset.page || page

    const selectedFacets = searchResultsElement?.dataset.facets
      ? JSON.parse(searchResultsElement.dataset.facets) as FilterSearchParams
      : {}

    const resultJSON = await fetchSearch(
      tabUrl,
      searchValue,
      +currentPage,
      queryParameterName,
      selectedFacets,
    )

    loadMoreButton.dataset.page = `${+currentPage + 1}`

    e.target?.dispatchEvent(
      createCustomEvent({
        name: events.loadMore,
        data: {
          page: `${+currentPage + 1}`,
          searchValue,
          tabUrl,
          selectedFacets,
          selectedSearchTab: document
            .querySelector('.cmp-saas')
            ?.getAttribute('data-selected-tab'),
        },
      }),
    )

    // we must search the results items wrapper from the current 'active' results element.
    const searchResultsItemsWrapper =
      searchResultsElement.getElementsByClassName(
        'cmp-saas__results-items',
      )?.[0]

    resultJSON?.results.forEach((resultItem: SearchItem) => {
      const searchItemElement = buildSearchItem(resultItem)
      searchResultsItemsWrapper.appendChild(searchItemElement)
    })

    if (searchResultsItemsWrapper) {
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
