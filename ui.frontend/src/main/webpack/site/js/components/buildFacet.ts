import { OnSearchItemClickCallback } from '../types/callbacks'
import { FilterFieldOption } from '../types/facetFilter'
import fetchSearch from '../utils/fetchSearch'
import buildFacetsGroup from './buildFacetsGroup'
import buildLoadMoreButton from './loadMoreButton'
import { generateSearchItemList } from './searchResults'

interface BuildFacetOption extends FilterFieldOption {
  filterFieldName: string
  tabUrl: string
  searchValue: string
  queryParameterName: string
  tabId: string
  loadMoreButtonText: string
  title: string
  onSearchItemClick?: OnSearchItemClickCallback
}

const buildFacet = ({
  value,
  hits,
  filterFieldName,
  tabUrl,
  searchValue,
  queryParameterName,
  tabId,
  onSearchItemClick,
  loadMoreButtonText,
  title,
}: // eslint-disable-next-line sonarjs/cognitive-complexity
BuildFacetOption): HTMLDivElement => {
  const facet = document.createElement('div')
  facet.classList.add('saas-facet')
  const facetInput = document.createElement('input')
  facetInput.classList.add('saas-facet-input')
  facetInput.type = 'checkbox'
  facetInput.id = value

  if (!hits) {
    facet.classList.add('saas-facet--no-result')
    facet.style.pointerEvents = 'none'
    facetInput.disabled = true
  }

  const selectedTab = document.querySelector<HTMLDivElement>(
    '[data-selected="true"]',
  )

  const selectedTabFacets = selectedTab?.dataset.facets
    ? JSON.parse(selectedTab.dataset.facets)
    : {}

  const selectedTabFacetsFieldName = selectedTabFacets[filterFieldName]
  facetInput.checked = selectedTabFacetsFieldName
    ? selectedTabFacetsFieldName.includes(value)
    : false

  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  facetInput.addEventListener('change', async (event) => {
    const isChecked = (event?.target as HTMLInputElement).checked

    const currentTab = document.querySelector<HTMLDivElement>(
      '[data-selected="true"]',
    )

    if (!currentTab) {
      return
    }

    const selectedFacets = currentTab?.dataset.facets
      ? JSON.parse(currentTab.dataset.facets)
      : {}

    const selectedFacetsFieldName = selectedFacets[filterFieldName] || []
    const updatedSelectedFacetsFieldName = isChecked
      ? [...selectedFacetsFieldName, value]
      : selectedFacetsFieldName.filter(
          (facetValue: string) => facetValue !== value,
        )

    const newSelectedFacetsValue = {
      ...selectedFacets,
      [filterFieldName]: updatedSelectedFacetsFieldName,
    }

    currentTab.dataset.facets = JSON.stringify(newSelectedFacetsValue)

    const results = await fetchSearch(
      tabUrl,
      searchValue,
      1,
      queryParameterName,
      newSelectedFacetsValue,
    )

    const currentTabResults = currentTab.querySelectorAll('*')

    currentTabResults?.forEach((element) => {
      element.remove()
    })

    if (results) {
      const facetsGroups = document.createElement('div')
      facetsGroups.classList.add('saas-facets-groups')

      const { facetFilters } = results

      facetFilters?.items.forEach((facetFilter) => {
        const facetsGroup = buildFacetsGroup({
          filterFieldLabel: facetFilter.filterFieldLabel,
          filterFieldName: facetFilter.filterFieldName,
          filterFieldOptions: facetFilter.filterFieldOptions,
          tabUrl,
          searchValue,
          queryParameterName: facetFilters.queryParameterName,
          tabId,
          onSearchItemClick,
          loadMoreButtonText,
          title,
        })

        facetsGroups?.appendChild(facetsGroup)
      })

      if (facetFilters) {
        currentTab.appendChild(facetsGroups)
      }

      const resultsItems = document.createElement('div')
      resultsItems.classList.add('saas-container_results_items')

      const searchResultsItem = generateSearchItemList(
        results.results,
        onSearchItemClick,
      )

      searchResultsItem.forEach((element) => {
        resultsItems.appendChild(element)
      })

      currentTab?.appendChild(resultsItems)

      if (results.showLoadMoreButton) {
        const loadMoreButton = buildLoadMoreButton({
          loadMoreButtonText,
          page: 2,
          tabUrl,
          searchValue,
          searchResultsElement: currentTab,
          onLoadMoreButtonClick: undefined,
          queryParameterName,
        })
        currentTab.appendChild(loadMoreButton)
      }
    }
  })

  const facetLabel = document.createElement('label')
  facetLabel.classList.add('saas-facet-label')
  facetLabel.innerText = `${value} (${hits})`
  facetLabel.htmlFor = value

  facet.appendChild(facetInput)
  facet.appendChild(facetLabel)

  return facet
}

export default buildFacet
