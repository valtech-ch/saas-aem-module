package com.valtech.aem.saas.api.fulltextsearch;

import java.util.List;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

@Builder
@Value
public class FulltextSearchResults {

  int totalResultsFound;

  int currentResultPage;

  @Singular
  List<Result> results;

}
