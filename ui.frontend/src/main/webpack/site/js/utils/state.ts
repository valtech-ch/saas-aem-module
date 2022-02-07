import { Tab } from '../components/searchTabs'
import { FacetFilters } from '../types/facetFilter'
import { merge } from './deepMerge'
import {
  getSessionStorage,
  setSessionStorage,
  STORAGE_FACET_FILTERS_KEY,
} from './sessionStorage'

type StateFacetFilters = {
  [key: string]: { [key: string]: number }
}

declare global {
  interface Window {
    appState: { facetFilters: StateFacetFilters }
  }
}

const defaultState = { facetFilters: {} }

const initAppState = () => {
  const appState = getSessionStorage({
    storageKey: STORAGE_FACET_FILTERS_KEY,
    defaultValue: '[{}]',
  })
  return Object.entries(appState).length === 0 &&
    appState.constructor === Object
    ? defaultState
    : appState
}

// ########################
// Current request response
// ########################
// {
//   facetFilters: {
//      items: [
//        {
//          filterFieldLabel: "Domain",
//          filterFieldName: "domain",
//          filterFieldOptions: [{value: "wknd.site", hits: 12}]
//        },
//        {
//          filterFieldLabel: "Content type",
//          filterFieldName: "contentType",
//          filterFieldOptions: [{value: "html", hits: 12}, {value: "pdf", hits: 2}]
//        },
//      ],
//      queryParameterName: 'facetFilter'
//   },
//   title: 'Search tab'
// }

// ########################
// Desirable data structure
// ########################
// 'Search tab': {
//   'Domain': {
//     'wknd.site': 12
//   },
//   'Content type': {
//     'html': 12,
//     'pdf': 2
//   }
// }
const transformFilterFacetsToMap = (facetFilters: FacetFilters | undefined) => {
  return (
    facetFilters?.items.reduce((acc1, item) => {
      const sortedFilterOptions = item.filterFieldOptions
        ?.sort((filterOption1, filterOption2) => {
          const filterOption1Value = filterOption1.value.toLowerCase()
          const filterOption2Value = filterOption2.value.toLowerCase()
          if (filterOption1Value > filterOption2Value) {
            return 1
          }

          if (filterOption2Value > filterOption1Value) {
            return -1
          }

          return 0
        })
        .reduce(
          (
            acc2: Record<string, unknown>,
            { value, hits }: { value: string; hits: number },
          ) => {
            return {
              ...acc2,
              ...{ [value]: hits },
            }
          },
          {},
        )
      return { ...acc1, ...{ [item.filterFieldLabel]: sortedFilterOptions } }
    }, {}) || null
  )
}

const resetFacetFiltersInAppState = (tabResult: Tab) => {
  const facetFilters = window.appState.facetFilters?.[tabResult.title]

  return Object.entries(facetFilters || {}).reduce(
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    (acc1, [facetGroupLabel, _]) => {
      const facetFilterItems = facetFilters[facetGroupLabel]
      const resetFacetFilters = {
        ...Object.entries(facetFilterItems).reduce(
          // eslint-disable-next-line @typescript-eslint/no-unused-vars, @typescript-eslint/no-shadow
          (acc2, [facetFilterLabel, _]) => {
            return {
              ...acc2,
              ...{ [facetFilterLabel]: 0 },
            }
          },
          {},
        ),
      }

      return { ...acc1, [facetGroupLabel]: resetFacetFilters }
    },
    {},
  )
}

export const handleFacetFiltersInAppState = (tabResult: Tab) => {
  const filterFieldOptions = transformFilterFacetsToMap(tabResult.facetFilters)

  if (!filterFieldOptions) {
    return
  }

  const resetCurrentFacetFilters = resetFacetFiltersInAppState(tabResult)

  const newState = {
    facetFilters: {
      [tabResult.title]: {
        ...merge(resetCurrentFacetFilters, filterFieldOptions),
      },
    },
  }

  window.appState = newState

  setSessionStorage({ storageKey: STORAGE_FACET_FILTERS_KEY, data: newState })
}

window.appState = initAppState()
