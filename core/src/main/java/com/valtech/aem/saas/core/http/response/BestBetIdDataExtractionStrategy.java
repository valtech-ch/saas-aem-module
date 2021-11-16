package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.util.Optional;

/**
 * A strategy for extracting best bets response data.
 */
public class BestBetIdDataExtractionStrategy implements
    SearchResponseDataExtractionStrategy<Integer> {

  public static final String ID = "id";

  @Override
  public String propertyName() {
    return ID;
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
