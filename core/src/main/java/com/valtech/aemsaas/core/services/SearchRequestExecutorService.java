package com.valtech.aemsaas.core.services;

import com.valtech.aemsaas.core.models.request.SearchRequest;
import com.valtech.aemsaas.core.models.response.search.SearchResponse;
import java.util.Optional;
import lombok.NonNull;

/**
 * Provides a method for execution of search requests.
 */
public interface SearchRequestExecutorService {

  /**
   * @param searchRequest http request.
   * @return optional SearchResponse object.
   */
  Optional<SearchResponse> execute(@NonNull SearchRequest searchRequest);

}
