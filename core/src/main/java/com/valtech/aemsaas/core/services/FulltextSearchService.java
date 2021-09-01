package com.valtech.aemsaas.core.services;

import com.valtech.aemsaas.core.models.search.FulltextSearchOptionalGetQuery;
import com.valtech.aemsaas.core.models.search.results.FulltextSearchResults;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface FulltextSearchService {

  Optional<FulltextSearchResults> getResults(String index, String term, String language,
      List<FulltextSearchOptionalGetQuery> optionalQueries);

  default Optional<FulltextSearchResults> getResults(String index, String term, String language,
      FulltextSearchOptionalGetQuery... queries) {
    return getResults(index, term, language, Arrays.asList(queries));
  }
}
