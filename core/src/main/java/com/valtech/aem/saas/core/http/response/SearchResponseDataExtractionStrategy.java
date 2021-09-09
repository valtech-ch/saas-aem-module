package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import java.util.Optional;

/**
 * A strategy for extracting specific data from a json formatted search response.
 *
 * @param <T> type of the POJO containing the extracted data.
 */
public interface SearchResponseDataExtractionStrategy<T> {

  /**
   * Returns the json property name containing the target data.
   *
   * @return json property name.
   */
  String propertyName();

  /**
   * Performs the extraction logic and returns the data in a predefined object type.
   *
   * @param response the response of a search request.
   * @return POJO containing the extracted data.
   */
  Optional<T> getData(JsonObject response);
}
