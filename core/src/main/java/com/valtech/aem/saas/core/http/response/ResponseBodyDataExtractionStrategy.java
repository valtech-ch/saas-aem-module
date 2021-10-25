package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.ResponseBodyDto;
import java.util.Optional;

/**
 * A strategy for extracting response body data.
 */
public final class ResponseBodyDataExtractionStrategy implements SearchResponseDataExtractionStrategy<ResponseBodyDto> {

  @Override
  public String propertyName() {
    return ResponseBodyDto.PN_RESPONSE;
  }

  @Override
  public Optional<ResponseBodyDto> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(JsonElement::getAsJsonObject)
        .map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseBodyDto.class));
  }
}
