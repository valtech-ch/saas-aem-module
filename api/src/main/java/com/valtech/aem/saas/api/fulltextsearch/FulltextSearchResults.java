package com.valtech.aem.saas.api.fulltextsearch;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

/**
 * POJO class representing the fulltext search results data. Actual results of type {@link Result} and results metadata:
 * current result page and the total number of results found.
 */
@Builder
@Value
public class FulltextSearchResults {

  int totalResultsFound;

  int currentResultPage;

  @Singular
  List<Result> results;

}
