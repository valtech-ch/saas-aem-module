package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import java.util.Optional;

/**
 * A strategy for extracting index update response data.
 */
public class DefaultIndexUpdateDataExtractionStrategy implements
    SearchResponseDataExtractionStrategy<IndexUpdateResponseDTO> {

  @Override
  public String propertyName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<IndexUpdateResponseDTO> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(jsonObject -> new Gson().fromJson(jsonObject, IndexUpdateResponseDTO.class));
  }
}
