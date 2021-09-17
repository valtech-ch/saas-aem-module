package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.valtech.aem.saas.api.indexing.IndexUpdateResponse;
import java.util.Optional;

/**
 * A strategy for extracting index update response data.
 */
public class DefaultIndexUpdateDataExtractionStrategy implements
    SearchResponseDataExtractionStrategy<IndexUpdateResponse> {

  @Override
  public String propertyName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Optional<IndexUpdateResponse> getData(JsonObject response) {
    return Optional.ofNullable(response)
        .map(jsonObject -> new Gson().fromJson(jsonObject, DefaultIndexUpdateResponse.class));
  }
}
