package com.valtech.aemsaas.core.models.response.search;

import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.response.parse.SearchResponseDataExtractionStrategy;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchResponse {

  @NonNull
  private final JsonObject response;

  public <T> Optional<T> get(SearchResponseDataExtractionStrategy<T> strategy) {
    return strategy.getData(response);
  }

}
