import { Tab } from '../components/searchTabs'
import { FacetFilters } from '../types/facetFilter'
import { merge } from './deepMerge'

export {}

declare global {
  interface Window {
    // { [key: string]: number } triggers error with ...window.appState.facetFilters[tabResult.title]
    appState: { facetFilters: any; initial: boolean }
  }
}

const defaultState = { facetFilters: {}, initial: true }

const handlers = {
  get: function (obj: any, prop: any) {
    return obj[prop]
  },
  set: function (obj: any, prop: any, value: any) {
    obj[prop] = value
    return true
  },
}

const transformFilterFacetsToMap = (facetFilters: FacetFilters | undefined) => {
  return (
    facetFilters?.items.reduce((acc, item) => {
      const result = item?.filterFieldOptions.reduce(
        (acc: {}, { value, hits }: { value: string; hits: number }) => {
          return {
            ...acc,
            ...{ [value]: hits },
          }
        },
        {},
      )
      return { ...acc, ...{ [item?.filterFieldLabel]: result } }
    }, {}) || null
  )
}

// 'search tab': {
//   'domain': {
//     'wknd.site': 0
//   },
//   'content type': {
//     'html': 12,
//     'pdf': 2
//   }
// }

const resetFacetFiltersInAppState = (tabResult: Tab) => {
  const facetFilters = window.appState.facetFilters[tabResult.title]

  return Object.entries(facetFilters || {}).reduce(
    (accumulator, [label, _]) => {
      const facetFilterItems = facetFilters[label]

      const resetFacetFilters = {
        ...Object.entries(facetFilterItems).reduce((acc, [key, _]) => {
          const resetFacetFilterItem = {
            ...acc,
            ...{ [key]: 0 },
          }
          return resetFacetFilterItem
        }, {}),
      }

      return { ...accumulator, [label]: resetFacetFilters }
    },
    {},
  )
}

// const resetFacetFiltersInAppState = (tabResult: Tab) => {
//   const facetFilters = window.appState.facetFilters[tabResult.title]

//   return Object.entries(facetFilters || {}).reduce((acc, [label, _]) => {
//     const facetFilterLabel = facetFilters[label]
//     return {
//       ...acc,
//       ...Object.entries(facetFilterLabel).reduce((acc, [key, _]) => {
//         return {
//           ...acc,
//           ...{ [key]: 0 },
//         }
//       }, {}),
//     }
//   }, {})
// }

export const handleFacetFiltersInAppState = (tabResult: Tab) => {
  const filterFieldOptions = transformFilterFacetsToMap(tabResult.facetFilters)

  // No need to handle searchTabs without facet filters
  if (!filterFieldOptions) {
    return
  }

  const resetCurrentFacetFilters = resetFacetFiltersInAppState(tabResult)

  const newState = {
    ...window.appState,
    facetFilters: {
      //...window.appState.facetFilters,
      queryParameterName: tabResult.facetFilters?.queryParameterName,
      [tabResult.title]: {
        ...merge(resetCurrentFacetFilters, filterFieldOptions),
      },
    },
  }

  window.appState = newState
}

const state = new Proxy(defaultState, handlers)

window.appState = state

window.addEventListener('unload', function () {
  window.appState = defaultState
})
