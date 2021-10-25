package com.valtech.aem.saas.core.fulltextsearch.dto;

import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.ResultDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.SuggestionDTO;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * Default implementation that offers a builder pattern based creation logic.
 */
@Builder
@Value
public class DefaultFulltextSearchResultsDTO implements FulltextSearchResultsDTO {

  int totalResultsFound;

  int currentResultPage;

  @Singular
  List<ResultDTO> results;

  SuggestionDTO suggestion;
}
