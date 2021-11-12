export type CallbackFunction = () => void

export type OnSearchCallback = (searchValue: string) => void
export type OnSwitchTabCallback = CallbackFunction
export type OnSearchItemClickCallback = (itemTitle: string) => void
export type OnLoadMoreButtonClickCallback = () => void

export type SearchCallbacks = {
  onSearch: OnSearchCallback
  onSwitchTab: OnSwitchTabCallback
  onSearchItemClick: OnSearchItemClickCallback
  onLoadMoreButtonClick: OnLoadMoreButtonClickCallback
}
