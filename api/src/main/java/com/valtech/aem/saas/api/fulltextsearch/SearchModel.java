package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.query.Filter;

import java.util.List;
import java.util.Set;

/**
 * Represents a model of the aem search component.
 */
public interface SearchModel extends SearchInputModel {

    /**
     * Retrieves the title configurable for the search component.
     *
     * @return configured title.
     */
    String getTitle();

    /**
     * Retrieves the language override configurable for the search component.
     *
     * @return configured language.
     */
    String getLanguage();

    /**
     * Retrieves search filter queries.
     *
     * @return set of filter details.
     */
    Set<Filter> getFilters();

    /**
     * Retrieves an author configured value for the max limit of results per request/page
     *
     * @return max number of results per request.
     */
    int getResultsPerPage();

    /**
     * Gets the label for the search button.
     *
     * @return search button text.
     */
    String getSearchButtonText();

    /**
     * Gets the label for the load more results button
     *
     * @return load more button text.
     */
    String getLoadMoreButtonText();

    /**
     * Gets list of search tabs;
     *
     * @return list of search tabs.
     */
    List<SearchTabModel> getSearchTabs();

    /**
     * Gets the suggestion text, with placeholder, when a misspelled search term is entered.
     *
     * @return suggestion text with placeholder.
     */
    String getAutoSuggestText();

    /**
     * Gets the text that is displayed when no results are found.
     *
     * @return no results text.
     */
    String getNoResultsText();

    /**
     * Gets the tracking url of the search component.
     * Tracking is done on client interaction with search results.
     *
     * @return url string.
     */
    String getTrackingUrl();

}
