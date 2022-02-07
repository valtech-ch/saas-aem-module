import { Tab } from '../components/searchTabs'
import { FacetFilters, FilterFieldOption } from '../types/facetFilter'

type StateFacetFilters = {
  [key: string]: { [key: string]: number }
}

declare global {
  interface Window {
    appState: { facetFilters: StateFacetFilters }
  }
}

const defaultState = { facetFilters: {} }

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
const transformFacetFilterGroupsToMap = (
  facetFilters: FacetFilters | undefined,
) => {
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
            acc2: Record<string, number>,
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

export const transformFacetFilterOptionsToMap = (
  filterFieldOptions: FilterFieldOption[] | undefined,
) => {
  return filterFieldOptions?.reduce(
    (
      acc: Record<string, number>,
      { value, hits }: { value: string; hits: number },
    ) => {
      return {
        ...acc,
        ...{ [value]: hits },
      }
    },
    {},
  )
}

export const resetFacetFilterOptionsByTitleAndFilterFieldLabel = ({
  title,
  filterFieldLabel,
}: {
  title: string
  filterFieldLabel: string
}): Record<string, number> => {
  const facetFilters = window.appState.facetFilters?.[title][filterFieldLabel]

  return Object.entries(facetFilters || {}).reduce(
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    (acc, [facetFilterOptionLabel, _]) => {
      return {
        ...acc,
        [facetFilterOptionLabel]: 0,
      }
    },
    {},
  )
}

export const handleFacetFiltersInAppState = (tabResult: Tab) => {
  if (!tabResult.facetFilters) {
    return
  }

  const newState = {
    facetFilters: {
      [tabResult.title]: {
        ...transformFacetFilterGroupsToMap(tabResult.facetFilters),
      },
    },
  }

  window.appState = newState
}

window.appState = defaultState
