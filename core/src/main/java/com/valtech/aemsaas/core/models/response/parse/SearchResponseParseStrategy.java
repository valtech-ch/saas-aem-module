package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.JsonObject;
import java.util.Optional;

public interface SearchResponseParseStrategy<T> {

  String propertyName();

  Optional<T> getResponse(JsonObject response);
}
