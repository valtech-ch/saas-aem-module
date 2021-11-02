package com.valtech.aem.saas.api.fulltextsearch.dto;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * Value object representing the fulltext search results data.
 */
@Builder
@Value
public class FulltextSearchResultsDTO {

  int totalResultsFound;

  int currentResultPage;

  @Singular
  List<ResultDTO> results;

  SuggestionDTO suggestion;
}
