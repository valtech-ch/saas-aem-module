(function webpackUniversalModuleDefinition(root, factory) {
	if(typeof exports === 'object' && typeof module === 'object')
		module.exports = factory();
	else if(typeof define === 'function' && define.amd)
		define([], factory);
	else {
		var a = factory();
		for(var i in a) (typeof exports === 'object' ? exports : root)[i] = a[i];
	}
})(this, function() {
return /******/ (function() { // webpackBootstrap
/******/ 	"use strict";
/******/ 	// The require scope
/******/ 	var __webpack_require__ = {};
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	!function() {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = function(exports) {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	}();
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};
// ESM COMPAT FLAG
__webpack_require__.r(__webpack_exports__);

;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchButton.ts
const buildSearchButton = ({
  searchButtonText
}) => {
  const searchButton = document.createElement('input');
  searchButton.classList.add('saas-container_button');
  searchButton.type = 'submit';
  searchButton.value = searchButtonText;
  return searchButton;
};

/* harmony default export */ var searchButton = (buildSearchButton);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/utils/fetchSearch.ts
const buildSearchUrl = (url, searchValue, offset) => {
  const urlElement = new URL(`${window.location.origin}${url}`);
  urlElement.searchParams.set('q', searchValue);

  if (offset) {
    urlElement.searchParams.set('start', offset.toString());
  }

  return urlElement.toString();
};

const fetchSearch = async (url, searchValue, offset) => {
  try {
    const searchURL = buildSearchUrl(url, searchValue, offset);
    const results = await fetch(searchURL);
    return await results.json();
  } catch {
    return null;
  }
};

/* harmony default export */ var utils_fetchSearch = (fetchSearch);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/utils/updateUrl.ts
const updateUrl = searchValue => {
  const currentUrl = new URL(window.location.href);
  const currentParams = new URLSearchParams(currentUrl.search);
  currentParams.set('q', searchValue);
  window.history.replaceState({}, '', `${window.location.pathname}?${currentParams.toString()}`);
};

/* harmony default export */ var utils_updateUrl = (updateUrl);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchItem.ts
const buildSearchItem = ({
  title,
  description,
  url,
  bestBet
}) => {
  const searchItem = document.createElement('div');
  searchItem.classList.add('saas-container_results_item');

  if (bestBet) {
    searchItem.classList.add('saas-container_results_item--best-bet');
  }

  const searchItemTitle = document.createElement('h3');
  searchItemTitle.classList.add('saas-container_results_item_title');
  searchItemTitle.innerHTML = title;
  const searchItemDescription = document.createElement('p');
  searchItemDescription.classList.add('saas-container_results_item_description');
  searchItemDescription.innerHTML = description;
  const searchItemUrl = document.createElement('a');
  searchItemUrl.classList.add('saas-container_results_item_url');
  searchItemUrl.innerHTML = url;
  searchItemUrl.href = url;
  searchItem.appendChild(searchItemTitle);
  searchItem.appendChild(searchItemDescription);
  searchItem.appendChild(searchItemUrl);
  return searchItem;
};
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/loadMoreButton.ts



const buildLoadMoreButton = ({
  loadMoreButtonText,
  offset,
  tabUrl,
  searchValue,
  searchResultsElement,
  onLoadMoreButtonClick
}) => {
  const loadMoreButton = document.createElement('button');
  loadMoreButton.classList.add('saas-load-more-button');
  loadMoreButton.dataset.offset = `${offset}`;
  loadMoreButton.innerText = loadMoreButtonText; // eslint-disable-next-line @typescript-eslint/no-misused-promises

  loadMoreButton.addEventListener('click', async event => {
    onLoadMoreButtonClick?.();
    event.preventDefault();
    const currentOffset = loadMoreButton.dataset.offset || offset;
    const resultJSON = await utils_fetchSearch(tabUrl, searchValue, +currentOffset);
    loadMoreButton.dataset.offset = `${+currentOffset + offset}`;
    resultJSON?.results.forEach(resultItem => {
      const searchItemElement = buildSearchItem(resultItem);
      searchResultsElement.insertBefore(searchItemElement, loadMoreButton);
    });

    if (!resultJSON?.showLoadMoreButton) {
      loadMoreButton.remove();
    }
  });
  return loadMoreButton;
};

/* harmony default export */ var components_loadMoreButton = (buildLoadMoreButton);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchResults.ts


const buildSearchResult = ({
  searchItems,
  tabId,
  onSearchItemClick
}) => {
  const searchResults = document.createElement('div');
  searchResults.classList.add('saas-container_results');
  searchResults.dataset.tab = tabId;
  searchItems.forEach(searchItem => {
    const searchItemElement = buildSearchItem(searchItem);

    if (onSearchItemClick) {
      searchItemElement.addEventListener('click', event => {
        event.preventDefault();
        onSearchItemClick?.(searchItem.title);
      });
    }

    searchResults.appendChild(searchItemElement);
  });
  return searchResults;
};

/* harmony default export */ var components_searchResults = (buildSearchResult);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchTabs.ts
const buildSearchTab = ({
  tabId,
  title,
  tabNumberOfResults,
  onSwitchTab
}) => {
  const searchTab = document.createElement('button');
  searchTab.classList.add('saas-container_tab');
  const searchTabName = document.createElement('span');
  searchTabName.innerHTML = title;
  const searchTabNumberOfResults = document.createElement('span');
  searchTabNumberOfResults.innerHTML = ` (${tabNumberOfResults})`;
  searchTab.appendChild(searchTabName);
  searchTab.appendChild(searchTabNumberOfResults);
  searchTab.addEventListener('click', () => {
    onSwitchTab?.();
    const searchTabs = document.querySelectorAll('.saas-container_results');
    const searchContainer = document.querySelector('.saas-container');

    if (searchContainer) {
      searchContainer.dataset.selectedTab = tabId;
    }

    searchTabs?.forEach(tab => {
      const tabElement = tab;

      if (tabElement.dataset.tab === tabId) {
        tabElement.style.display = 'block';
        return;
      }

      tabElement.style.display = 'none';
    });
  });
  return searchTab;
};

const removeAutosuggest = () => {
  const autoSuggestElement = document.querySelector('.saas-autosuggest');
  autoSuggestElement?.remove();
};
const removeSearchTabs = () => {
  const searchTabs = document.querySelectorAll('.saas-container_tab');
  searchTabs.forEach(tab => {
    tab.remove();
  });
};
const removeSearchResults = () => {
  const searchResults = document.querySelectorAll('.saas-container_results');
  searchResults.forEach(searchResult => {
    searchResult.remove();
  });
};
const removeSelectedTabFromSearchContainer = () => {
  const searchContainer = document.querySelector('.saas-container');

  if (searchContainer) {
    searchContainer.removeAttribute('data-selected-tab');
  }
};
/* harmony default export */ var searchTabs = (buildSearchTab);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchResultsTab.ts




const buildSearchResultsTab = ({
  tabResult,
  searchValue,
  searchForm,
  searchFormParent,
  loadMoreButtonText,
  onSearchItemClick,
  onSwitchTab,
  onLoadMoreButtonClick
}) => {
  const {
    resultsTotal,
    showLoadMoreButton,
    tabId,
    title,
    results,
    url
  } = tabResult;

  if (resultsTotal) {
    const searchContainer = document.querySelector('.saas-container');

    if (searchContainer && tabResult.index === 0) {
      searchContainer.dataset.selectedTab = tabResult.tabId;
    }

    const searchTabElement = searchTabs({
      tabId,
      tabNumberOfResults: resultsTotal,
      title,
      onSwitchTab
    });
    const searchResults = components_searchResults({
      searchItems: results,
      tabId,
      onSearchItemClick
    });
    searchForm?.parentNode?.insertBefore(searchTabElement, searchForm.nextSibling);
    searchFormParent?.appendChild(searchResults);

    if (showLoadMoreButton) {
      const loadMoreButton = components_loadMoreButton({
        loadMoreButtonText,
        offset: 1,
        tabUrl: url,
        searchValue,
        searchResultsElement: searchResults,
        onLoadMoreButtonClick
      });
      searchResults?.appendChild(loadMoreButton);
    }

    if (searchContainer?.dataset.selectedTab !== tabId) {
      searchResults.style.display = 'none';
    }
  }
};

/* harmony default export */ var searchResultsTab = (buildSearchResultsTab);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchSuggestion.ts
const buildSearchSuggestion = (suggestionText, autoSuggestText) => {
  const suggestionUrl = new URL(window.location.href);
  suggestionUrl.searchParams.set('q', suggestionText);
  const autoSuggestElement = document.createElement('div');
  autoSuggestElement.classList.add('saas-autosuggest');
  const autoSuggestLink = document.createElement('a');
  autoSuggestLink.innerText = `${autoSuggestText} ${suggestionText}`;
  autoSuggestLink.href = suggestionUrl.toString();
  autoSuggestElement.appendChild(autoSuggestLink);
  return autoSuggestElement;
};

/* harmony default export */ var searchSuggestion = (buildSearchSuggestion);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchForm.ts






const buildSearchForm = () => {
  const searchForm = document.createElement('form');
  searchForm.classList.add('saas-container_form');
  return searchForm;
};

const triggerSearch = async (searchForm, searchInputElement, searchUrl, searchTabs, loadMoreButtonText, autoSuggestText, options) => {
  if (searchInputElement.dataset.loading === 'true') {
    return;
  }

  const searchInputElementCopy = searchInputElement;
  searchInputElementCopy.dataset.loading = 'true';
  const searchValue = searchInputElement.value;
  const {
    onSearch,
    onSwitchTab,
    onSearchItemClick,
    onLoadMoreButtonClick
  } = options || {};
  onSearch?.(searchValue);

  if (searchUrl) {
    window.location.href = searchUrl;
    return;
  }

  utils_updateUrl(searchValue);
  removeAutosuggest();
  removeSearchTabs();
  removeSearchResults();
  removeSelectedTabFromSearchContainer();
  const tabResultsArray = await Promise.all(searchTabs.map(async (tab, index) => {
    const tabResultsJSON = await utils_fetchSearch(tab.url, searchValue);
    return { ...tabResultsJSON,
      tabId: tab.title,
      index
    };
  })).finally(() => {
    searchInputElementCopy.dataset.loading = 'false';
  });
  const searchFormParent = searchForm.parentElement;
  const hasResults = tabResultsArray.some(tab => tab.resultsTotal);

  if (!hasResults && tabResultsArray?.[0]?.suggestion) {
    const autoSuggestElement = searchSuggestion(tabResultsArray[0].suggestion.text, autoSuggestText);
    searchFormParent?.append(autoSuggestElement);
    return;
  }

  tabResultsArray.sort((tab1, tab2) => {
    if (tab1.index < tab2.index) {
      return 1;
    }

    if (tab2.index < tab1.index) {
      return -1;
    }

    return 0;
  }).forEach(tabResult => {
    searchResultsTab({
      tabResult,
      searchValue,
      searchForm,
      searchFormParent,
      loadMoreButtonText,
      onSearchItemClick,
      onSwitchTab,
      onLoadMoreButtonClick
    });
  });
};
const addEventToSearchForm = (searchForm, searchInputElement, searchUrl, searchTabs, loadMoreButtonText, autoSuggestText, options) => {
  // eslint-disable-next-line @typescript-eslint/no-misused-promises
  return searchForm.addEventListener('submit', event => {
    event.preventDefault();
    return triggerSearch(searchForm, searchInputElement, searchUrl, searchTabs, loadMoreButtonText, autoSuggestText, options);
  });
};
/* harmony default export */ var searchForm = (buildSearchForm);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/utils/debounce.ts
/* eslint-disable no-void,@typescript-eslint/ban-ts-comment */
function debounce(func, timeout = 500) {
  let timer;
  return (...args) => {
    clearTimeout(timer);
    timer = setTimeout(() => {
      // @ts-ignore
      void func.apply(this, args);
    }, timeout);
  };
}

/* harmony default export */ var utils_debounce = (debounce);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/utils/fetchAutoSuggest.ts
const buildAutoSuggestUrl = (url, query) => {
  const urlElement = new URL(`${window.location.origin}${url}`);
  urlElement.searchParams.set('q', query);
  return urlElement.toString();
};

const fetchAutoSuggest = async (url, query) => {
  try {
    const searchURL = buildAutoSuggestUrl(url, query);
    const results = await fetch(searchURL);
    const resultsJSON = await results.json();
    return resultsJSON.filter(result => result.length);
  } catch {
    return null;
  }
};

/* harmony default export */ var utils_fetchAutoSuggest = (fetchAutoSuggest);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/components/searchInput.ts


const SUGGESTION_ELEMENT_CLASS = 'saas-suggestions-element';
const ACTIVE_SUGGESTION_ELEMENT_CLASS = `${SUGGESTION_ELEMENT_CLASS}--active`;

const setSaasCurrentFocusSuggestion = (inputElement, value) => {
  inputElement.setAttribute('saasCurrentFocusSuggestion', value.toString());
};

const getSaasCurrentFocusSuggestion = inputElement => {
  return inputElement.getAttribute('saasCurrentFocusSuggestion') ?? '-1';
};

const removeSuggestionList = () => {
  const existingDataList = document.querySelector('.saas-container_form #suggestions');

  if (existingDataList) {
    existingDataList.remove();
  }
};

const debouncedSearch = autoSuggestionDebounceTime => utils_debounce(async (autosuggestUrl, query, autocompleteTriggerThreshold, searchInput) => {
  removeSuggestionList();
  setSaasCurrentFocusSuggestion(searchInput, -1);

  if (query.length >= autocompleteTriggerThreshold) {
    const results = await utils_fetchAutoSuggest(autosuggestUrl, query);

    if (results?.length) {
      const suggestionDropdown = document.createElement('div');
      suggestionDropdown.id = 'suggestions';
      results.forEach(result => {
        const suggestionDropdownElement = document.createElement('div');
        suggestionDropdownElement.innerText = result;
        suggestionDropdownElement.classList.add(SUGGESTION_ELEMENT_CLASS);
        suggestionDropdownElement.addEventListener('click', () => {
          const searchInputElementCopy = searchInput;
          removeSuggestionList();
          searchInputElementCopy.value = result;
        });
        suggestionDropdown.appendChild(suggestionDropdownElement);
      });
      searchInput.after(suggestionDropdown);
    }
  }
}, autoSuggestionDebounceTime);

const buildSearchInput = ({
  searchFieldPlaceholderText,
  autosuggestUrl,
  autocompleteTriggerThreshold,
  autoSuggestionDebounceTime = 500
}) => {
  const searchInput = document.createElement('input');
  searchInput.placeholder = searchFieldPlaceholderText;
  searchInput.id = 'saas-search-input';
  searchInput.autocomplete = 'off';
  setSaasCurrentFocusSuggestion(searchInput, -1);
  const search = debouncedSearch(autoSuggestionDebounceTime);
  searchInput.addEventListener('input', event => {
    search(autosuggestUrl, event?.target?.value, autocompleteTriggerThreshold, searchInput);
  });
  document.addEventListener('click', () => {
    /* Remove the autocomplete list from DOM when a click happens in the document */
    removeSuggestionList();
  });
  searchInput.addEventListener('keydown', e => {
    const DOWN_ARROW = 'ArrowDown';
    const UP_ARROW = 'ArrowUp';
    const ENTER_KEY = 'Enter';
    const suggestionElements = document.querySelectorAll(`#suggestions .${SUGGESTION_ELEMENT_CLASS}`);

    if (!suggestionElements.length) {
      return;
    }

    const minFocus = -1;
    const maxFocus = suggestionElements.length - 1;
    const currentFocusAttr = getSaasCurrentFocusSuggestion(searchInput);
    const currentFocus = parseInt(currentFocusAttr, 10);
    const suggestionElement = suggestionElements[currentFocus];

    if (e.key === DOWN_ARROW && currentFocus < maxFocus) {
      const newFocus = currentFocus + 1;
      const newSuggestionElement = suggestionElements[newFocus];
      suggestionElement?.classList.remove(ACTIVE_SUGGESTION_ELEMENT_CLASS);
      newSuggestionElement?.classList.add(ACTIVE_SUGGESTION_ELEMENT_CLASS);
      setSaasCurrentFocusSuggestion(searchInput, newFocus);
    }

    if (e.key === UP_ARROW && currentFocus > minFocus) {
      const newFocus = currentFocus - 1;
      const newSuggestionElement = suggestionElements[newFocus];
      suggestionElement?.classList.remove(ACTIVE_SUGGESTION_ELEMENT_CLASS);
      newSuggestionElement?.classList.add(ACTIVE_SUGGESTION_ELEMENT_CLASS);
      setSaasCurrentFocusSuggestion(searchInput, newFocus);
    }

    if (e.key === ENTER_KEY && suggestionElement) {
      setSaasCurrentFocusSuggestion(searchInput, -1);
      suggestionElement.click();
    }
  });
  return searchInput;
};

/* harmony default export */ var searchInput = (buildSearchInput);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/searchElement.ts
const getSearchElement = (searchSelector = 'search') => {
  return document.querySelector(searchSelector);
};
const isSearchConfig = searchConfig => {
  const {
    searchFieldPlaceholderText,
    loadMoreButtonText,
    searchTabs,
    searchButtonText,
    autocompleteTriggerThreshold,
    autosuggestUrl,
    searchUrl,
    autoSuggestText = 'sdf'
  } = searchConfig;
  return typeof searchFieldPlaceholderText === 'string' && Boolean(searchFieldPlaceholderText) && typeof loadMoreButtonText === 'string' && Boolean(loadMoreButtonText) && Array.isArray(searchTabs) && typeof searchButtonText === 'string' && Boolean(searchButtonText) && typeof autocompleteTriggerThreshold === 'number' && (!searchUrl || typeof searchUrl === 'string') && typeof autosuggestUrl === 'string' && Boolean(autosuggestUrl) && typeof autoSuggestText === 'string' && Boolean(autoSuggestText);
};
const getDataAttributeFromSearchElement = element => {
  if (!element) {
    return null;
  }

  const searchConfigStr = element.dataset.searchConfig || '{}';
  const searchConfig = JSON.parse(searchConfigStr);
  const isSearchConfigValid = isSearchConfig(searchConfig);

  if (!isSearchConfigValid) {
    return null;
  }

  return searchConfig;
};
;// CONCATENATED MODULE: ./src/main/webpack/site/js/utils/saasStyle.ts
const initSaasStyle = () => {
  document.styleSheets[0].insertRule(`
            #suggestions {
              position: absolute;
              border: 1px solid #d4d4d4;
              border-bottom: none;
              border-top: none;
              z-index: 99;
              top: 100%;
              left: 0;
              right: 0;
            }
          `, 0);
  document.styleSheets[0].insertRule(`
            .saas-autocomplete {
               position: relative;
               display: inline-block;
            }
          `, 0);
  document.styleSheets[0].insertRule(`
            #suggestions .saas-suggestions-element {
              padding: 10px;
              cursor: pointer;
              border-bottom: 1px solid #d4d4d4;
              background: #fff;
            }
          `, 0);
  document.styleSheets[0].insertRule(`
            #suggestions .saas-suggestions-element:hover {
              background-color: #e9e9e9;
            }
          `, 0);
  document.styleSheets[0].insertRule(`
            .saas-suggestions-element--active {
              background-color: DodgerBlue !important;
              color: #fff;
            }
          `, 0);
};

/* harmony default export */ var saasStyle = (initSaasStyle);
;// CONCATENATED MODULE: ./src/main/webpack/site/js/buildSearch.ts





const buildSearch = async (searchElement, options) => {
  const searchConfig = getDataAttributeFromSearchElement(searchElement);

  if (!searchConfig) {
    return;
  }

  const {
    callbacks,
    autoSuggestionDebounceTime = 500
  } = options || {};
  const {
    searchFieldPlaceholderText,
    searchButtonText,
    searchUrl,
    searchTabs,
    loadMoreButtonText,
    autosuggestUrl,
    autocompleteTriggerThreshold,
    autoSuggestText
  } = searchConfig;
  const searchContainer = document.createElement('div');
  searchContainer.classList.add('saas-container');
  const searchFormElement = searchForm();
  const searchAutocompleteWrapper = document.createElement('div');
  searchAutocompleteWrapper.classList.add('saas-autocomplete');
  const searchInputElement = searchInput({
    searchFieldPlaceholderText,
    autosuggestUrl,
    autocompleteTriggerThreshold,
    autoSuggestionDebounceTime
  });
  const searchButtonElement = searchButton({
    searchButtonText
  });
  addEventToSearchForm(searchFormElement, searchInputElement, searchUrl, searchTabs, loadMoreButtonText, autoSuggestText, callbacks);
  saasStyle();
  searchAutocompleteWrapper.appendChild(searchInputElement);
  searchFormElement.appendChild(searchAutocompleteWrapper);
  searchFormElement.appendChild(searchButtonElement);
  const searchElementParent = searchElement.parentElement;
  searchElementParent?.replaceChild(searchContainer, searchElement);
  searchContainer.appendChild(searchFormElement);
  const currentUrl = new URL(window.location.href);
  const currentParams = new URLSearchParams(currentUrl.search);
  const searchValue = currentParams.get('q');

  if (searchValue) {
    searchInputElement.value = searchValue;
    await triggerSearch(searchFormElement, searchInputElement, searchUrl, searchTabs, loadMoreButtonText, autoSuggestText, callbacks);
  }
};
;// CONCATENATED MODULE: ./src/main/webpack/site/js/initSearch.ts



exports.default = function initSearch() {
  window.addEventListener('load', () => {
    const searchElement = getSearchElement();

    if (!searchElement) {
      return;
    }

    buildSearch(searchElement);
  });
};
/******/ 	return __webpack_exports__;
/******/ })()
;
});