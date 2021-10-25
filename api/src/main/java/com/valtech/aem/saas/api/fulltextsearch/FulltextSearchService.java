package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchPayloadDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import java.util.Optional;
import lombok.NonNull;

/**
 * Represents a service that performs fulltext search queries and retrieves the according results.
 */
public interface FulltextSearchService {

  /**
   * Executes a fulltext search for specified payload (request parameters).
   *
   * @param index                    name of the index in SaaS.
   * @param fulltextSearchPayloadDto object containing query parameters
   * @param enableAutoSuggest        flag for enabling auto suggest
   * @param enableBestBets           flag for enabling best bets
   * @return search related data.
   */
  Optional<FulltextSearchResultsDTO> getResults(@NonNull String index,
      @NonNull FulltextSearchPayloadDTO fulltextSearchPayloadDto,
      boolean enableAutoSuggest,
      boolean enableBestBets);

}
