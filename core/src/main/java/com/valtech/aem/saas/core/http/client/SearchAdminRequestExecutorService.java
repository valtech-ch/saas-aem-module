package com.valtech.aem.saas.core.http.client;

import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import lombok.NonNull;

import java.util.Optional;

/**
 * Represents a service for executing search requests on SaaS.
 */
public interface SearchAdminRequestExecutorService {

    /**
     * Gets the base url/domain of the search api.
     *
     * @return
     */
    String getBaseUrl();

    /**
     * Returns the response from SaaS.
     *
     * @param searchRequest http request.
     * @return SearchResponse optional, which is empty if an error/exception occured during request execution.
     */
    Optional<SearchResponse> execute(@NonNull SearchRequest searchRequest);

}
