type AutosuggestResult = string[]

const buildAutoSuggestUrl = (url: string, query: string) => {
  const urlElement = new URL(`${window.location.origin}${url}`)

  urlElement.searchParams.set('q', query)

  return urlElement.toString()
}

const fetchAutoSuggest = async (
  url: string,
  query: string,
): Promise<AutosuggestResult | null> => {
  try {
    const searchURL = buildAutoSuggestUrl(url, query)
    const results = await fetch(searchURL)

    return (await results.json()) as AutosuggestResult
  } catch {
    return null
  }
}

export default fetchAutoSuggest
