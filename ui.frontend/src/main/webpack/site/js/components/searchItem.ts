export type SearchItem = {
  title: string
  description: string
  url: string
}

export const buildSearchItem = ({
  title,
  description,
  url,
}: SearchItem): HTMLDivElement => {
  const searchItem = document.createElement('div')

  const searchItemTitle = document.createElement('h2')
  searchItemTitle.innerHTML = title

  const searchItemDescription = document.createElement('p')
  searchItemDescription.innerHTML = description

  const searchItemUrl = document.createElement('a')
  searchItemUrl.innerHTML = url
  searchItemUrl.href = url

  searchItem.appendChild(searchItemTitle)
  searchItem.appendChild(searchItemDescription)
  searchItem.appendChild(searchItemUrl)

  return searchItem
}
