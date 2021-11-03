package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.FacetCountsDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * A strategy for extracting facet counts data.
 */
@RequiredArgsConstructor
public final class FacetCountsDataExtractionStrategy implements SearchResponseDataExtractionStrategy<FacetCountsDTO> {

  @Override
  public String propertyName() {
    return FacetCountsDTO.PN_FACET_COUNTS;
  }

  @Override
  public Optional<FacetCountsDTO> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(JsonElement::getAsJsonObject)
        .map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, FacetCountsDTO.class));
  }

}
