package com.valtech.aemsaas.core.services.search;

import com.valtech.aemsaas.core.models.search.payload.FulltextSearchGetRequestPayload;
import com.valtech.aemsaas.core.models.search.results.FulltextSearchResults;
import java.util.Optional;

/**
 * Represents a service that performs fulltext search queries and retrieves the according results.
 */
public interface FulltextSearchService {

  /**
   * Executes a fulltext search for a specified index and payload (request parameters).
   *
   * @param index                           name of the index in SaaS.
   * @param fulltextSearchGetRequestPayload object containing query parameters
   * @return search related data.
   */
  Optional<FulltextSearchResults> getResults(String index,
      FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload);
}
