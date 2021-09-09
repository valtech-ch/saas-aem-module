package com.valtech.aem.saas.core.http.request;

import lombok.NonNull;
import org.apache.http.client.methods.HttpUriRequest;

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
}
