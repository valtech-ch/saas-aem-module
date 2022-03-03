package com.valtech.aem.saas.api.caconfig;

import com.valtech.aem.saas.api.query.Filter;

import java.util.Set;

/**
 * A model that provides context-aware search configurations.
 */
public interface SearchCAConfigurationModel {

    /**
     * Retrieves configured index value. Index is considered required.
     *
     * @return saas index
     * @throws IllegalStateException thrown when index value is blank.
     */
    String getIndex();

    /**
     * Retrieves configured project id. Project id is considered required.
     *
     * @return saas project id
     * @throws IllegalStateException thrown when project id is blank.
     */
    int getProjectId();

    /**
     * Retrieves set of query ready items.
     *
     * @return set of filter entries.
     */
    Set<Filter> getFilters();

    /**
     * Retrieves the name of the tag which will be used to highlight text matches in resulting search content.
     *
     * @return html tag name.
     */
    String getHighlightTagName();

    /**
     * Checks whether best bets feature is enabled.
     *
     * @return true if enabled.
     */
    boolean isBestBetsEnabled();

    /**
     * Checks whether auto suggest feature is enabled.
     *
     * @return true if enabled.
     */
    boolean isAutoSuggestEnabled();

    /**
     * Checks whether autocomplete feature is enabled.
     *
     * @return true if enabled.
     */
    boolean isAutocompleteEnabled();

    /**
     * Gets the autocomplete trigger threshold.
     * That is the minimum number of characters entered before displaying a selection of autocomplete
     * options/suggestions.
     *
     * @return positive integer.
     */
    int getAutocompleteThreshold();

    /**
     * Gets the autocomplete max total allowed.
     *
     * @return positive integer.
     */
    int getAutocompleteResultsMaxTotal();

    /**
     * Checks whether search result items click tracking is enabled.
     *
     * @return true if enabled.
     */
    boolean isSearchResultItemTrackingEnabled();
}
