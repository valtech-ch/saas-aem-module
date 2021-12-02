package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.ResponseHeaderDTO;

import java.util.Optional;

/**
 * A strategy for extracting response header data.
 */
public final class ResponseHeaderDataExtractionStrategy implements
        SearchResponseDataExtractionStrategy<ResponseHeaderDTO> {

    @Override
    public String propertyName() {
        return ResponseHeaderDTO.PN_RESPONSE_HEADER;
    }

    @Override
    public Optional<ResponseHeaderDTO> getData(JsonElement response) {
        return Optional.ofNullable(response)
                       .filter(JsonElement::isJsonObject)
                       .map(JsonElement::getAsJsonObject)
                       .map(r -> r.getAsJsonObject(propertyName()))
                       .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseHeaderDTO.class));
    }
}
