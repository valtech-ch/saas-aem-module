package com.valtech.aemsaas.core.models.responses.search;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.util.Optional;

public class ResponseBodyParseStrategy implements SearchResponseParseStrategy<ResponseBody> {

  @Override
  public String propertyName() {
    return "responseBody";
  }

  @Override
  public Optional<ResponseBody> getResponse(JsonElement response) {
    return Optional.ofNullable(response.getAsJsonObject().get(propertyName()))
        .map(jsonElement -> new Gson().fromJson(jsonElement, ResponseBody.class));
  }
}
