package com.valtech.aem.saas.core.caconfig;

import com.valtech.aem.saas.api.caconfig.SearchFilterConfiguration;
import com.valtech.aem.saas.core.fulltextsearch.MultiValueFilterConfiguration;

/**
 * A POJO for a multi-value filter entry in context aware configuration
 * {@link com.valtech.aem.saas.api.caconfig.SearchConfiguration}.
 */
public class CaFilterConfigurationModel extends MultiValueFilterConfiguration {

    /**
     * Creates a filter configuration object out of a context aware configuration filter entry.
     *
     * @param searchFilterConfiguration filter entry
     */
    public CaFilterConfigurationModel(SearchFilterConfiguration searchFilterConfiguration) {
        super(searchFilterConfiguration.name(), searchFilterConfiguration.value());
    }
}
