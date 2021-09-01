package com.valtech.aemsaas.core.models.responses.search;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Optional;

public interface SearchResponseParseStrategy<T> {

  String propertyName();

  Optional<T> getResponse(JsonElement response);
}
