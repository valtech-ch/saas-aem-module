package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ComponentExporter;

import static com.valtech.aem.saas.api.fulltextsearch.SearchModel.AUTOCOMPLETE_THRESHOLD;

/**
 * Represents a model of the aem search redirect component.
 */
public interface SearchRedirectModel extends ComponentExporter {

    /**
     * Gets an author configured value for component's  input field's placeholder.
     *
     * @return placeholder text.
     */
    String getSearchFieldPlaceholderText();

    /**
     * Gets the autocomplete trigger threshold.
     *
     * @return min number of chars typed before triggering the autocomplete.
     */
    default int getAutocompleteTriggerThreshold() {
        return AUTOCOMPLETE_THRESHOLD;
    }

    /**
     * Gets the auto suggest url of the search component.
     *
     * @return url string.
     */
    String getAutocompleteUrl();

    /**
     * Gets the search configuration in a json format. In this format, the configs are used by the FE.
     *
     * @return json formatted string.
     */
    String getConfigJson();

    /**
     * Gets the search page's url which the component redirects to.
     *
     * @return page url.
     */
    String getSearchUrl();

    /**
     * Returns a boolean flag whether to render the cmp's markup
     *
     * @return true when it should be rendered
     */
    boolean render();
}
