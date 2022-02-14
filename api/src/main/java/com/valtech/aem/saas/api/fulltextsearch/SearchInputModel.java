package com.valtech.aem.saas.api.fulltextsearch;

/**
 * Interface of a component containing search input field.
 */
public interface SearchInputModel {

    /**
     * Gets the auto-complete trigger threshold.
     *
     * @return min number of chars typed before triggering the auto-complete.
     */
    int getAutoCompleteTriggerThreshold();

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
    String getAutoCompleteUrl();

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
