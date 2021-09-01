package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.JsonElement;
import java.util.Optional;

public interface SearchResponseParseStrategy<T> {

  String propertyName();

  Optional<T> getResponse(JsonElement response);
}
