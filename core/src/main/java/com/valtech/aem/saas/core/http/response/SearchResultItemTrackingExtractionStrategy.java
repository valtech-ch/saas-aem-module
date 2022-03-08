package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.api.tracking.dto.SearchResultItemTrackingDTO;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * A strategy for extracting search result item tracking data.
 */
@RequiredArgsConstructor
public final class SearchResultItemTrackingExtractionStrategy implements SearchResponseDataExtractionStrategy<SearchResultItemTrackingDTO> {

    @Override
    public String propertyName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<SearchResultItemTrackingDTO> getData(JsonElement response) {
        return Optional.ofNullable(response)
                       .filter(JsonElement::isJsonObject)
                       .map(JsonElement::getAsJsonObject)
                       .map(jsonObject -> new Gson().fromJson(jsonObject, SearchResultItemTrackingDTO.class));
    }

}
