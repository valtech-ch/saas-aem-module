import { QUERY_PARAM_SEARCH_TERM } from '../constants'

const updateUrl = (searchValue: string): void => {
  const currentUrl = new URL(window.location.href)
  const currentParams = new URLSearchParams(currentUrl.search)
  currentParams.set(QUERY_PARAM_SEARCH_TERM, searchValue)

  window.history.replaceState(
    {},
    '',
    `${window.location.pathname}?${currentParams.toString()}`,
  )
}

export default updateUrl
