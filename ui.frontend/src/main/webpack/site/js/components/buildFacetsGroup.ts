import { OnSearchItemClickCallback } from '../types/callbacks'
import { FacetItem } from '../types/facetFilter'
import buildFacet from './buildFacet'

interface BuildFacetsGroup extends FacetItem {
  tabUrl: string
  searchValue: string
  queryParameterName: string
  tabId: string
  onSearchItemClick?: OnSearchItemClickCallback
}

const buildFacetsGroup = ({
  filterFieldLabel,
  filterFieldName,
  filterFieldOptions,
  tabUrl,
  searchValue,
  queryParameterName,
  tabId,
  onSearchItemClick,
}: BuildFacetsGroup): HTMLDivElement => {
  const facetItem = document.createElement('div')
  facetItem.classList.add('saas-facets-group')

  const facetItemTitle = document.createElement('h4')
  facetItemTitle.innerText = filterFieldLabel
  facetItemTitle.classList.add('saas-facets-group-title')

  facetItem.appendChild(facetItemTitle)

  filterFieldOptions.forEach(({ value, hits }) => {
    const facet = buildFacet({
      value,
      hits,
      filterFieldName,
      tabUrl,
      searchValue,
      queryParameterName,
      tabId,
      onSearchItemClick,
    })

    facetItem.appendChild(facet)
  })

  return facetItem
}

export default buildFacetsGroup
