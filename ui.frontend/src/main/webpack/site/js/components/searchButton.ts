type SearchButtonOptions = {
  searchButtonText: string
}

const buildSearchButton = ({
  searchButtonText,
}: SearchButtonOptions): HTMLButtonElement => {
  const searchButton = document.createElement('button')

  searchButton.innerHTML = searchButtonText

  return searchButton
}

export default buildSearchButton
