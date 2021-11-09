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
}: SearchItem): HTMLDivElement => {
  const searchItem = document.createElement('div')
  searchItem.classList.add('saas-container_results_item')

  const searchItemTitle = document.createElement('h3')
  searchItemTitle.classList.add('saas-container_results_item_title')
  searchItemTitle.innerHTML = title

  const searchItemDescription = document.createElement('p')
  searchItemDescription.classList.add('saas-container_results_item_description')
  searchItemDescription.innerHTML = description

  const searchItemUrl = document.createElement('a')
  searchItemUrl.classList.add('saas-container_results_item_url')
  searchItemUrl.innerHTML = url
  searchItemUrl.href = url

  searchItem.appendChild(searchItemTitle)
  searchItem.appendChild(searchItemDescription)
  searchItem.appendChild(searchItemUrl)

  return searchItem
}
