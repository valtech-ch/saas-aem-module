package com.valtech.aem.saas.api.fulltextsearch;

import java.util.Optional;

public interface FulltextSearchConsumerService {

  /**
   * Executes a fulltext search for specified payload (request parameters).
   *
   * @param fulltextSearchGetRequestPayload object containing query parameters
   * @return search related data.
   */
  Optional<FulltextSearchResults> getResults(FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload);
}
