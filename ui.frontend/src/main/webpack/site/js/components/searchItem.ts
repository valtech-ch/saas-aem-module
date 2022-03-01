export type SearchItem = {
  title: string
  description: string
  url: string
  bestBet: boolean
  trackingUrlParam: string
}

export const buildSearchItem = ({
  title,
  description,
  url,
  bestBet,
  trackingUrlParam
}: SearchItem): HTMLDivElement => {
  const searchItem = document.createElement('div')
  searchItem.classList.add('cmp-saas__results-item')

  if (bestBet) {
    searchItem.classList.add('cmp-saas__results-item--best-bet')
  }

  const searchItemUrlCite = document.createElement('span')
  searchItemUrlCite.classList.add('cmp-saas__results-item-link-text')
  searchItemUrlCite.innerHTML = url

  const searchItemTitle = document.createElement('h3')
  searchItemTitle.classList.add('cmp-saas__results-item-title')
  searchItemTitle.innerHTML = title

  const searchItemDescription = document.createElement('p')
  searchItemDescription.classList.add('cmp-saas__results-item-description')
  searchItemDescription.innerHTML = description

  const searchItemUrl = document.createElement('a')
  searchItemUrl.classList.add('cmp-saas__results-item-url')
  //searchItemUrl.href = url

  searchItemUrl.appendChild(searchItemUrlCite)
  searchItemUrl.appendChild(searchItemTitle)
  searchItemUrl.appendChild(searchItemDescription)
  searchItem.appendChild(searchItemUrl)

  searchItem.addEventListener("click", () => {
     fetch(trackingUrl, {
      method: 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ clickedUrl: url})
    })
  .then(response => response.json())
        .then(data => {
          console.log('Success:', data);
        })
        .catch((error) => {
          console.error('Error:', error);
        })

  })


  return searchItem
}