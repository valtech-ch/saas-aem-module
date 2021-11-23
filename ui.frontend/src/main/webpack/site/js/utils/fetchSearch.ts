import { Tab } from '../components/searchTabs'

type FilterSearchParams = {
  [key: string]: string[]
}

const buildSearchUrl = (
  url: string,
  searchValue: string,
  offset?: number,
  queryParameterName?: string,
  filterSearchParams?: FilterSearchParams,
) => {
  const urlElement = new URL(`${window.location.origin}${url}`)

  urlElement.searchParams.set('q', searchValue)

  if (offset) {
    urlElement.searchParams.set('start', offset.toString())
  }

  if (queryParameterName && filterSearchParams) {
    Object.keys(filterSearchParams).forEach((filter) => {
      const filterValue = filterSearchParams[filter].toString()

      urlElement.searchParams.append(
        queryParameterName,
        `${filter}:${filterValue}`,
      )
    })
  }

  return urlElement.toString()
}

const fetchSearch = async (
  url: string,
  searchValue: string,
  offset?: number,
  queryParameterName?: string,
  filterSearchParams?: FilterSearchParams,
): Promise<Tab | null> => {
  try {
    const searchURL = buildSearchUrl(
      url,
      searchValue,
      offset,
      queryParameterName,
      filterSearchParams,
    )
    const results = await fetch(searchURL)

    return (await results.json()) as Tab
  } catch {
    return null
  }
}

export default fetchSearch
