type AutocCompleteResult = string[]

const buildAutoCompletetUrl = (url: string, query: string) => {
  const urlElement = new URL(`${window.location.origin}${url}`)

  urlElement.searchParams.set('q', query)

  return urlElement.toString()
}

const fetchAutoComplete = async (
  url: string,
  query: string,
): Promise<AutocCompleteResult | null> => {
  try {
    const searchURL = buildAutoCompletetUrl(url, query)
    const results = await fetch(searchURL)

    const resultsJSON = (await results.json()) as AutocCompleteResult

    return resultsJSON.filter((result) => result.length)
  } catch {
    return null
  }
}

export default fetchAutoComplete
