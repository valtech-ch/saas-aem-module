import { OnSearchItemClickCallback } from '../types/callbacks'
import { FilterFieldOption } from '../types/facetFilter'
import fetchSearch from '../utils/fetchSearch'
import buildSearchResult from './searchResults'

interface BuildFacetOption extends FilterFieldOption {
  filterFieldName: string
  tabUrl: string
  searchValue: string
  queryParameterName: string
  tabId: string
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
}: BuildFacetOption): HTMLDivElement => {
  const facet = document.createElement('div')
  facet.classList.add('saas-facet')

  const facetInput = document.createElement('input')
  facetInput.classList.add('saas-facet-input')
  facetInput.type = 'checkbox'
  facetInput.id = value

  facetInput.addEventListener('change', async (event) => {
    const isChecked = (event?.target as HTMLInputElement).checked

    const currentTab = document.querySelector<HTMLDivElement>(
      '[data-selected="true"]',
    )

    const selectedFacets = currentTab?.dataset.facets
      ? JSON.parse(currentTab.dataset.facets)
      : {}

    const selectedFacetsFieldName = selectedFacets[filterFieldName]

    const newSelectedFacetsValue = {
      ...selectedFacets,
      [filterFieldName]: selectedFacetsFieldName
        ? [...selectedFacetsFieldName, value]
        : [value],
    }

    if (currentTab) {
      currentTab.dataset.facets = JSON.stringify(newSelectedFacetsValue)
    }

    const results = await fetchSearch(
      tabUrl,
      searchValue,
      0,
      queryParameterName,
      newSelectedFacetsValue,
    )

    // Update Facets

    // clear existing results
    const currentTabResults = currentTab?.querySelectorAll('*')

    currentTabResults?.forEach((element) => {
      element.remove()
    })

    if (results) {
      const searchResults = buildSearchResult({
        searchItems: results.results,
        tabId,
        onSearchItemClick,
      })

      currentTab?.appendChild(searchResults)
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
