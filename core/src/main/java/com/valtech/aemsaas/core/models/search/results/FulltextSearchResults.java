package com.valtech.aemsaas.core.models.search.results;

import java.util.HashMap;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
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
