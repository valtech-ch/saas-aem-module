package com.valtech.aem.saas.core.http.request;

import lombok.NonNull;
import java.util.List;
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

  /**
   * Gets a list of http resoponse status codes that will be considered as response success
   *
   * @return list of integers.
   */
  List<Integer> getSuccessStatusCodes();
}
