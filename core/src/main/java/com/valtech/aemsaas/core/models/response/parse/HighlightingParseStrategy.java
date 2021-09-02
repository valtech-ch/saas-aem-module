package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.response.search.Highlighting;
import java.util.Optional;

public class HighlightingParseStrategy implements SearchResponseParseStrategy<Highlighting> {

  @Override
  public String propertyName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<Highlighting> getResponse(JsonObject response) {
    return Optional.ofNullable(response)
        .map(jsonObject -> new Gson().fromJson(jsonObject, Highlighting.class));
  }
}
