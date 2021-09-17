package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchResponse {

  private final JsonObject response;
  @Getter
  private final boolean success;

  public <T> Optional<T> get(SearchResponseDataExtractionStrategy<T> strategy) {
    return Optional.ofNullable(response).flatMap(strategy::getData);
  }

}
