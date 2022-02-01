package com.valtech.aem.saas.core.caconfig;

import com.valtech.aem.saas.api.caconfig.SearchFilterConfiguration;
import com.valtech.aem.saas.core.fulltextsearch.MultiValueFilterConfiguration;

public class CaFilterConfigurationModel extends MultiValueFilterConfiguration {

    public CaFilterConfigurationModel(SearchFilterConfiguration searchFilterConfiguration) {
        super(searchFilterConfiguration.name(), searchFilterConfiguration.value());
    }
}
