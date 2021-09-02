package com.valtech.aemsaas.core.models.response.parse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.response.search.ResponseBody;
import java.util.Optional;

public class ResponseBodyParseStrategy implements SearchResponseParseStrategy<ResponseBody> {

  @Override
  public String propertyName() {
    return "response";
  }

  @Override
  public Optional<ResponseBody> getResponse(JsonObject response) {
    return Optional.ofNullable(response).map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, ResponseBody.class));
  }
}
