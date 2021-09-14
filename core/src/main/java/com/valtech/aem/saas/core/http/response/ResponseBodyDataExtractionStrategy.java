package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.Optional;

/**
 * A strategy for extracting response body data.
 */
public class ResponseBodyDataExtractionStrategy implements SearchResponseDataExtractionStrategy<ResponseBody> {

  @Override
  public String propertyName() {
    return ResponseBody.PN_RESPONSE;
  }

  @Override
  public Optional<ResponseBody> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(JsonElement::getAsJsonObject)
        .map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseBody.class));
  }
}
