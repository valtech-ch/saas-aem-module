package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
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
