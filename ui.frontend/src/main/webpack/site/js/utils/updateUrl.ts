const updateUrl = (searchValue: string): void => {
  const currentUrl = new URL(window.location.href)
  const currentParams = new URLSearchParams(currentUrl.search)
  currentParams.set('q', searchValue)

  window.history.replaceState(
    {},
    '',
    `${window.location.pathname}?${currentParams.toString()}`,
  )
}

export default updateUrl
