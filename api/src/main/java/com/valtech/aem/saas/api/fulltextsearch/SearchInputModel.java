package com.valtech.aem.saas.api.fulltextsearch;

/**
 * Interface of a component containing search input field.
 */
public interface SearchInputModel {
    /**
     * The minimum search term length for which autocomplete is performed
     */
    int AUTOCOMPLETE_THRESHOLD = 3;

    /**
     * Gets the autocomplete trigger threshold.
     *
     * @return min number of chars typed before triggering the autocomplete.
     */
    default int getAutocompleteTriggerThreshold() {
        return AUTOCOMPLETE_THRESHOLD;
    }

    /**
     * Gets the search configuration in a json format. In this format, the configs are used by the FE.
     *
     * @return json formatted string.
     */
    String getConfigJson();

    /**
     * Gets the auto suggest url of the search component.
     *
     * @return url string.
     */
    String getAutocompleteUrl();

    /**
     * Gets the unique id of the component, based on resource path.
     *
     * @return unique identifier
     */
    String getId();

    /**
     * Gets an author configured value for search input field's placeholder.
     *
     * @return placeholder text.
     */
    String getSearchFieldPlaceholderText();

}
