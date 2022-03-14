export const events = {
  tabSwitch: 'saas-tab-switch',
  searchResultItemClick: 'saas-search-result-item-click',
  facetsSelect: 'saas-facets-select',
  autoSuggestSelect: 'saas-autosuggest-select',
  searchSubmit: 'saas-search-submit',
  loadMore: 'saas-load-more-click',
}

export const createCustomEvent = ({
  name,
  data,
}: {
  name: string
  data?: any
}): Event => {
  return new CustomEvent(name, {
    bubbles: true,
    detail: data,
  })
}
