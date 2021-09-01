package com.valtech.aemsaas.core.services;

import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.responses.search.SearchResponse;
import com.valtech.aemsaas.core.models.search.FulltextSearchGetQuery;
import com.valtech.aemsaas.core.models.search.results.FulltextSearchResults;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface FulltextSearchService {

  Optional<FulltextSearchResults> getResults(String index, List<FulltextSearchGetQuery> queries);

  default Optional<FulltextSearchResults> getResults(String index, FulltextSearchGetQuery... queries) {
    return getResults(index, Arrays.asList(queries));
  }
}
