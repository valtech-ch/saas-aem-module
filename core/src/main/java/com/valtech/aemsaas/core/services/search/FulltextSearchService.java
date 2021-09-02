package com.valtech.aemsaas.core.services.search;

import com.valtech.aemsaas.core.models.search.payload.FulltextSearchGetRequestPayload;
import com.valtech.aemsaas.core.models.search.results.FulltextSearchResults;
import java.util.Optional;

public interface FulltextSearchService {

  Optional<FulltextSearchResults> getResults(String index,
      FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload);
}
