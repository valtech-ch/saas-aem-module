package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.valtech.aem.saas.api.bestbets.BestBet;
import com.valtech.aem.saas.core.bestbets.BestBetDetails;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

/**
 * A strategy for extracting index update response data.
 */
public class BestBetsDataExtractionStrategy implements
    SearchResponseDataExtractionStrategy<List<BestBet>> {

  @Override
  public String propertyName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<List<BestBet>> getData(JsonElement response) {
    Type type = new TypeToken<List<BestBetDetails>>() {
    }.getType();
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonArray)
        .map(JsonElement::getAsJsonArray)
        .map(jsonArray -> new Gson().fromJson(jsonArray, type));
  }
}
