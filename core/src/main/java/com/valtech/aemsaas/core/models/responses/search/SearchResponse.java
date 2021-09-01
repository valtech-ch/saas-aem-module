package com.valtech.aemsaas.core.models.responses.search;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchResponse {

  private final JsonObject response;

  public <T> Optional<T> get(SearchResponseParseStrategy<T> strategy) {
    return strategy.getResponse(response);
  }

//  ResponseHeader responseHeader;
//
//  ResponseBody response;
//
//  FacetCounts facetCounts;
//
//  Map<String, Map<String, List<String>>> highlighting;

}
