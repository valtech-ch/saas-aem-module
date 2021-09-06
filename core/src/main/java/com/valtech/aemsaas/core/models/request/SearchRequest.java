package com.valtech.aemsaas.core.models.request;

import lombok.NonNull;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Defines a base type for a search request.
 */
public interface SearchRequest {

  /**
   * Gets the prepared http request.
   *
   * @return http request.
   */
  @NonNull HttpUriRequest getRequest();
}
