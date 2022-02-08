type SearchButtonOptions = {
  searchButtonText: string
}

const buildSearchButton = ({
  searchButtonText,
}: SearchButtonOptions): HTMLButtonElement => {
  const searchButton = document.createElement('button')
  searchButton.classList.add('saas-container__button')

  searchButton.type = 'submit'
  searchButton.textContent = searchButtonText

  return searchButton
}

export default buildSearchButton
