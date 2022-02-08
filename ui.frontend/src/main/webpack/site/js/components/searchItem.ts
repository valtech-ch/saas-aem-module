export type SearchItem = {
  title: string
  description: string
  url: string
  bestBet: boolean
}

export const buildSearchItem = ({
  title,
  description,
  url,
  bestBet,
}: SearchItem): HTMLDivElement => {
  const searchItem = document.createElement('div')
  searchItem.classList.add('saas-container__results-item')

  if (bestBet) {
    searchItem.classList.add('saas-container__results-item--best-bet')
  }

  const searchItemUrlCite = document.createElement('span')
  searchItemUrlCite.classList.add('saas-container__results-item-link-text')
  searchItemUrlCite.innerHTML = url

  const searchItemTitle = document.createElement('h3')
  searchItemTitle.classList.add('saas-container__results-item-title')
  searchItemTitle.innerHTML = title

  const searchItemDescription = document.createElement('p')
  searchItemDescription.classList.add(
    'saas-container__results-item-description',
  )
  searchItemDescription.innerHTML = description

  const searchItemUrl = document.createElement('a')
  searchItemUrl.classList.add('saas-container__results-item-url')
  searchItemUrl.href = url

  searchItemUrl.appendChild(searchItemUrlCite)
  searchItemUrl.appendChild(searchItemTitle)
  searchItemUrl.appendChild(searchItemDescription)
  searchItem.appendChild(searchItemUrl)

  return searchItem
}
