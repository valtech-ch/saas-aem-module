import { Tab } from '../components/searchTabs'

const buildSearchUrl = (url: string, searchValue: string, offset?: number) => {
  const urlElement = new URL(`${window.location.origin}${url}`)

  urlElement.searchParams.set('q', searchValue)

  if (offset) {
    urlElement.searchParams.set('start', offset.toString())
  }

  return urlElement.toString()
}

const fetchSearch = async (
  url: string,
  searchValue: string,
  offset?: number,
): Promise<Tab | null> => {
  try {
    const searchURL = buildSearchUrl(url, searchValue, offset)
    const results = await fetch(searchURL)

    return (await results.json()) as Tab
  } catch {
    return null
  }
}

export default fetchSearch
