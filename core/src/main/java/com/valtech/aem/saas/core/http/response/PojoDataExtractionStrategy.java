package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.FacetCountsDTO;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * A strategy for extracting facet counts data.
 */
@RequiredArgsConstructor
public final class PojoDataExtractionStrategy<T> implements SearchResponseDataExtractionStrategy<T> {

    private final Class<T> clazz;

    @Override
    public String propertyName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<T> getData(JsonElement response) {
        return Optional.ofNullable(response)
                       .filter(JsonElement::isJsonObject)
                       .map(JsonElement::getAsJsonObject)
                       .map(r -> r.getAsJsonObject(propertyName()))
                       .map(jsonObject -> new Gson().fromJson(jsonObject, clazz));
    }

}
