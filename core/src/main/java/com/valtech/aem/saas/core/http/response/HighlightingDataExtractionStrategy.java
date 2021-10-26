package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.HighlightingDTO;
import java.util.Optional;

/**
 * A strategy for extracting highlighting data.
 */
public final class HighlightingDataExtractionStrategy implements SearchResponseDataExtractionStrategy<HighlightingDTO> {

  @Override
  public String propertyName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<HighlightingDTO> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(jsonObject -> new Gson().fromJson(jsonObject, HighlightingDTO.class));
  }
}
