package com.valtech.aemsaas.core.models.search.payload;

/**
 * Represents a payload object for the fulltext search request.
 */
public interface FulltextSearchGetRequestPayload {

  /**
   * Returns the payload as a get request query string.
   *
   * @return query string.
   */
  String getPayload();

  /**
   * V
   *
   * @return true if payload is properly set up.
   */
  boolean validate();
}
