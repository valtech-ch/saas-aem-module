package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Optional;

/**
 * A strategy for extracting highlighting data.
 */
public final class JsonObjectDataExtractionStrategy implements SearchResponseDataExtractionStrategy<JsonObject> {

    @Override
    public String propertyName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<JsonObject> getData(JsonElement response) {
        return Optional.ofNullable(response)
                       .filter(JsonElement::isJsonObject)
                       .map(JsonElement::getAsJsonObject);
    }
}
