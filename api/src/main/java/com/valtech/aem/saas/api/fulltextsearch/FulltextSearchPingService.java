package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import lombok.NonNull;

/**
 * Represents a service for executing ping request to SaaS.
 */
public interface FulltextSearchPingService {

    /**
     * Checks connection to api.
     *
     * @return true if api is accessed.
     */
    boolean ping(@NonNull SearchCAConfigurationModel searchConfiguration);
}
