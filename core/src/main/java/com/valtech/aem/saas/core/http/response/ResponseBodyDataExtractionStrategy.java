package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.ResponseBodyDTO;

import java.util.Optional;

/**
 * A strategy for extracting response body data.
 */
public final class ResponseBodyDataExtractionStrategy implements SearchResponseDataExtractionStrategy<ResponseBodyDTO> {

    @Override
    public String propertyName() {
        return ResponseBodyDTO.PN_RESPONSE;
    }

    @Override
    public Optional<ResponseBodyDTO> getData(JsonElement response) {
        return Optional.ofNullable(response)
                       .filter(JsonElement::isJsonObject)
                       .map(JsonElement::getAsJsonObject)
                       .map(r -> r.getAsJsonObject(propertyName()))
                       .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseBodyDTO.class));
    }
}
