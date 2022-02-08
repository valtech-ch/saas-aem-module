import { OnSearchItemClickCallback } from '../types/callbacks'
import { FacetItem } from '../types/facetFilter'
import {
  resetFacetFilterOptionsByTitleAndFilterFieldLabel,
  transformFacetFilterOptionsToMap,
} from '../utils/state'
import buildFacet from './buildFacet'

interface BuildFacetsGroup extends FacetItem {
  tabUrl: string
  searchValue: string
  queryParameterName: string
  tabId: string
  loadMoreButtonText: string
  title: string
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
  loadMoreButtonText,
  title,
}: BuildFacetsGroup): HTMLDivElement => {
  const facetItem = document.createElement('div')
  facetItem.classList.add('saas-facets-group')

  const facetItemTitle = document.createElement('h4')
  facetItemTitle.innerText = filterFieldLabel
  facetItemTitle.classList.add('saas-facets-group__title')

  facetItem.appendChild(facetItemTitle)

  const resetFacetFilterOptions =
    resetFacetFilterOptionsByTitleAndFilterFieldLabel({
      title,
      filterFieldLabel,
    })
  const facetFilterOptionsMap =
    transformFacetFilterOptionsToMap(filterFieldOptions)

  const newFacetFilterOptionsState = {
    ...resetFacetFilterOptions,
    ...facetFilterOptionsMap,
  }

  Object.entries(newFacetFilterOptionsState).forEach(
    ([value, hits]: [value: string, hits: number]) => {
      const facet = buildFacet({
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
      })

      facetItem.appendChild(facet)
    },
  )

  return facetItem
}

export default buildFacetsGroup
