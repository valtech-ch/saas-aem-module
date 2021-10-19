package com.valtech.aem.saas.api.fulltextsearch;

import java.util.Optional;
import lombok.NonNull;

/**
 * Represents a service that performs fulltext search queries and retrieves the according results.
 */
public interface FulltextSearchService {

  /**
   * Executes a fulltext search for specified payload (request parameters).
   *
   * @param index         name of the index in SaaS.
   * @param fulltextSearchGetRequestPayload object containing query parameters
   * @param enableAutoSuggest flag for enabling auto suggest
   * @param enableBestBets flag for enabling best bets
   * @return search related data.
   */
  Optional<FulltextSearchResults> getResults(@NonNull String index,
      @NonNull FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload,
      boolean enableAutoSuggest,
      boolean enableBestBets);

}
