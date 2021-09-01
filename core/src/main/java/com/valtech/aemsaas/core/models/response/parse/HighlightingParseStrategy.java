package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HighlightingParseStrategy implements SearchResponseParseStrategy<Map<String, Map<String, List<String>>>> {

  @Override
  public String propertyName() {
    return "highlighting";
  }

  @Override
  public Optional<Map<String, Map<String, List<String>>>> getResponse(JsonElement response) {
    Type highlightingMapType = new TypeToken<Map<String, Map<String, List<String>>>>() {
    }.getType();
    return Optional.ofNullable(response.getAsJsonObject().get(propertyName()))
        .map(jsonElement -> new Gson().fromJson(jsonElement, highlightingMapType));
  }
}
