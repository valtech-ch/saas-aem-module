package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.response.search.ResponseHeader;
import java.util.Optional;

public class ResponseHeaderParseStrategy implements SearchResponseParseStrategy<ResponseHeader> {

  @Override
  public String propertyName() {
    return "responseHeader";
  }

  @Override
  public Optional<ResponseHeader> getResponse(JsonObject response) {
    return Optional.ofNullable(response).map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseHeader.class));
  }
}
