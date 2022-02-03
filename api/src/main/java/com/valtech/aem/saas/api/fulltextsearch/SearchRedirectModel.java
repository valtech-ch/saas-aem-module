package com.valtech.aem.saas.api.fulltextsearch;

/**
 * Represents a model of the aem search redirect component.
 */
public interface SearchRedirectModel extends SearchInputModel {

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
