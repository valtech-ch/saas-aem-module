const buildSearchSuggestion = (
  suggestionText: string,
  autoSuggestText: string,
): HTMLDivElement => {
  const suggestionUrl = new URL(window.location.href)
  suggestionUrl.searchParams.set('q', suggestionText)

  const autoSuggestElement = document.createElement('div')
  autoSuggestElement.classList.add('saas-autosuggest')

  const autoSuggestLink = document.createElement('a')
  autoSuggestLink.innerText = `${autoSuggestText} ${suggestionText}`
  autoSuggestLink.href = suggestionUrl.toString()
  autoSuggestElement.appendChild(autoSuggestLink)

  return autoSuggestElement
}

export default buildSearchSuggestion
