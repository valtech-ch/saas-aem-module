type SearchButtonOptions = {
  searchButtonText: string
}

const buildSearchButton = ({
  searchButtonText,
}: SearchButtonOptions): HTMLInputElement => {
  const searchButton = document.createElement('input')
  searchButton.classList.add('saas-container_button')

  searchButton.type = 'submit'
  searchButton.value = searchButtonText

  return searchButton
}

export default buildSearchButton