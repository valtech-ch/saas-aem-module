package com.valtech.aemsaas.core.models.response.search;

import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.response.parse.SearchResponseParseStrategy;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchResponse {

  @NonNull
  private final JsonObject response;

  public <T> Optional<T> get(SearchResponseParseStrategy<T> strategy) {
    return strategy.getResponse(response);
  }

}
