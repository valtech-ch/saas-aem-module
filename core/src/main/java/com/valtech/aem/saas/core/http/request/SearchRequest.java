package com.valtech.aem.saas.core.http.request;

import lombok.NonNull;
import org.apache.http.client.methods.HttpUriRequest;

import java.util.List;

/**
 * Represents a base type for a search request.
 */
public interface SearchRequest {

    /**
     * Gets the prepared http request.
     *
     * @return http request.
     */
    @NonNull HttpUriRequest getRequest();

    /**
     * Gets a list of http resoponse status codes that will be considered as response success
     *
     * @return list of integers.
     */
    List<Integer> getSuccessStatusCodes();
}
