package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aemsaas.core.models.response.search.ResponseHeader;
import java.util.Optional;

public class ResponseHeaderParseStrategy implements SearchResponseParseStrategy<ResponseHeader> {

  @Override
  public String propertyName() {
    return "responseHeader";
  }

  @Override
  public Optional<ResponseHeader> getResponse(JsonElement response) {
    return Optional.ofNullable(response.getAsJsonObject().get(propertyName()))
        .map(jsonElement -> new Gson().fromJson(jsonElement, ResponseHeader.class));
  }
}
