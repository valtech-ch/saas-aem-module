package com.valtech.aemsaas.core.services.search;

import com.valtech.aemsaas.core.models.request.SearchRequest;
import com.valtech.aemsaas.core.models.response.search.SearchResponse;
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
