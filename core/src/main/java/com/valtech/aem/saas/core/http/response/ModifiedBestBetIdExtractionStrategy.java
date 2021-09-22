package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Optional;

/**
 * A strategy for extracting the best bet's id.
 */
public class ModifiedBestBetIdExtractionStrategy implements
    SearchResponseDataExtractionStrategy<Integer> {

  public static final String PN_ID = "id";

  @Override
  public String propertyName() {
    return PN_ID;
  }

  @Override
  public Optional<Integer> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(JsonElement::getAsJsonObject)
        .map(r -> r.getAsJsonPrimitive(propertyName()))
        .map(JsonPrimitive::getAsInt);
  }
}
