package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.query.Filter;

/**
 * Represents a sling resource that contains pre-configurable search filter details.
 */
public interface FilterConfiguration {

    /**
     * Gets a {@link Filter} object constructed from the name and value
     *
     * @return query filter object.
     */
    Filter getFilter();

    /**
     * Checks whether the filter has the necessary data.
     *
     * @return true if name and value are specified.
     */
    boolean isValid();
}
