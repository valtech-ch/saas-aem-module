package com.valtech.aem.saas.core.http.client;

import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import java.util.Optional;
import lombok.NonNull;

/**
 * Represents a service for executing search requests on SaaS.
 */
public interface SearchRequestExecutorService {

  /**
   * Returns the response from SaaS.
   *
   * @param searchRequest http request.
   * @return optional SearchResponse object.
   */
  Optional<SearchResponse> execute(@NonNull SearchRequest searchRequest);

}
