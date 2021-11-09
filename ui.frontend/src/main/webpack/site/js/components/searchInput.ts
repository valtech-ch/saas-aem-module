type SearchInputOptions = {
  searchFieldPlaceholderText: string
}

const buildSearchInput = ({
  searchFieldPlaceholderText,
}: SearchInputOptions): HTMLInputElement => {
  const searchInput = document.createElement('input')

  searchInput.placeholder = searchFieldPlaceholderText

  return searchInput
}

export default buildSearchInput
