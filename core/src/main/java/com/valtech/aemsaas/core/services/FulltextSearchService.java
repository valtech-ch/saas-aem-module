package com.valtech.aemsaas.core.services;

import com.valtech.aemsaas.core.models.responses.search.SearchResponse;
import com.valtech.aemsaas.core.models.search.FulltextSearchGetQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface FulltextSearchService {

  Optional<SearchResponse> getResults(String index, List<FulltextSearchGetQuery> queries);

  default Optional<SearchResponse> getResults(String index, FulltextSearchGetQuery... queries) {
    return getResults(index, Arrays.asList(queries));
  }
}
