import { QUERY_PARAM_SEARCH_TERM } from '../constants'
import { createCustomEvent } from '../service/serviceEvent'

const buildSearchSuggestion = (
  suggestionText: string,
  autoSuggestText: string,
): HTMLDivElement => {
  const suggestionUrl = new URL(window.location.href)
  suggestionUrl.searchParams.set(QUERY_PARAM_SEARCH_TERM, suggestionText)

  const autoSuggestElement = document.createElement('div')
  autoSuggestElement.classList.add('cmp-saas__autosuggest')

  const autoSuggestLink = document.createElement('a')
  autoSuggestLink.innerText = `${autoSuggestText} ${suggestionText}`
  autoSuggestLink.href = suggestionUrl.toString()
  autoSuggestElement.appendChild(autoSuggestLink)

  autoSuggestElement.addEventListener('click', (e) => {
    e.target?.dispatchEvent(
      createCustomEvent({
        name: suggestionText,
        data: {
          autoSuggestText,
        },
      }),
    )
  })

  return autoSuggestElement
}

export default buildSearchSuggestion
