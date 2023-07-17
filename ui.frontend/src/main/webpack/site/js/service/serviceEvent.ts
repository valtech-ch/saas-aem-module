export const events = {
  tabSwitch: 'saas-tab-switch',
  searchResultItemClick: 'saas-search-result-item-click',
  facetsSelect: 'saas-facets-select',
  autoSuggestSelect: 'saas-autosuggest-select',
  searchSubmit: 'saas-search-submit',
  loadMore: 'saas-load-more-click',
}

export const createCustomEvent = <T>({
  name,
  data,
}: {
  name: string
  data?: T
}): Event => {
  return new CustomEvent(name, {
    bubbles: true,
    detail: data,
  })
}
