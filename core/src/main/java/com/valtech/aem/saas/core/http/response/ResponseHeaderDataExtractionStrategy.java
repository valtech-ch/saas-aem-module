package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.Optional;

/**
 * A strategy for extracting response header data.
 */
public class ResponseHeaderDataExtractionStrategy implements SearchResponseDataExtractionStrategy<ResponseHeader> {

  @Override
  public String propertyName() {
    return ResponseHeader.PN_RESPONSE_HEADER;
  }

  @Override
  public Optional<ResponseHeader> getData(JsonObject response) {
    return Optional.ofNullable(response)
        .map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseHeader.class));
  }
}
