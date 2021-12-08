package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonElement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SearchResponse {

    private final JsonElement response;
    @Getter
    private final boolean success;

    public <T> Optional<T> get(SearchResponseDataExtractionStrategy<T> strategy) {
        return Optional.ofNullable(response).flatMap(strategy::getData);
    }

}
