package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.response.search.Highlighting;
import java.util.Optional;

/**
 * A strategy for extracting highlighting data.
 */
public class HighlightingDataExtractionStrategy implements SearchResponseDataExtractionStrategy<Highlighting> {

  @Override
  public String propertyName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Highlighting> getData(JsonObject response) {
    return Optional.ofNullable(response)
        .map(jsonObject -> new Gson().fromJson(jsonObject, Highlighting.class));
  }
}
