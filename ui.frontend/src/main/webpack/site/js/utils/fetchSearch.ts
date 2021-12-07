import { Tab } from '../components/searchTabs'

type FilterSearchParams = {
  [key: string]: string[]
}

const buildSearchUrl = (
    url: string,
    searchValue: string,
    page?: number,
    queryParameterName?: string,
    filterSearchParams?: FilterSearchParams,
) => {
  const urlElement = new URL(`${window.location.origin}${url}`)

  urlElement.searchParams.set('q', searchValue)

  if (page) {
    urlElement.searchParams.set('page', page.toString())
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
    page?: number,
    queryParameterName?: string,
    filterSearchParams?: FilterSearchParams,
): Promise<Tab | null> => {
  try {
    const searchURL = buildSearchUrl(
        url,
        searchValue,
        page,
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
