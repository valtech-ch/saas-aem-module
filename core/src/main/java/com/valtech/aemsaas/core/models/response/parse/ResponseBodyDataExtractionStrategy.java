package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.response.search.ResponseBody;
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
  public Optional<ResponseBody> getData(JsonObject response) {
    return Optional.ofNullable(response)
        .map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseBody.class));
  }
}
