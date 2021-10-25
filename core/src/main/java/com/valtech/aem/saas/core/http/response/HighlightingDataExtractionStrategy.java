package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.HighlightingDto;
import java.util.Optional;

/**
 * A strategy for extracting highlighting data.
 */
public final class HighlightingDataExtractionStrategy implements SearchResponseDataExtractionStrategy<HighlightingDto> {

  @Override
  public String propertyName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<HighlightingDto> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(jsonObject -> new Gson().fromJson(jsonObject, HighlightingDto.class));
  }
}
