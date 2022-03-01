import { buildSearch } from './buildSearch'
import {getDataAttributeFromSearchElement, getSearchElements} from './searchElement'
import { cleanSessionStorage } from './utils/sessionStorage'
import {OnLoadMoreButtonClickCallback, OnSearchCallback, OnSwitchTabCallback} from "./types/callbacks";

function initSearch(): void {
  window.addEventListener('load', () => {
    const searchElements = getSearchElements()
    cleanSessionStorage()

    if (!searchElements) {
      return
    }

    searchElements?.forEach((searchElement) => {
      const searchConfig = getDataAttributeFromSearchElement(searchElement)
      if (!searchConfig) {
        return
      }
      const { trackingUrl } = searchConfig
      if (!trackingUrl) {
        return;
      }

      const username = 'admin';
      const password = 'admin';

      const headers = new Headers();
      headers.set('Authorization', 'Basic ' + username + ":" + password);
      headers.append('Content-Type', 'application/json');
      void buildSearch(searchElement as HTMLDivElement,
          searchConfig,
          {callbacks:
            {onSearchItemClick: (param) => {
            fetch(trackingUrl, {
              method: 'POST',
              headers: headers,
              body: JSON.stringify({ trackedUrl: param})
            })
                .then(response => response.json())
                .then(data => {
                  console.log('Success:', data);
                })
                .catch((error) => {
                  console.error('Error:', error);
                })
          }}, autoSuggestionDebounceTime: 500})
    })
  })
}

export default initSearch
