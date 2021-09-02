package com.valtech.aemsaas.core.models.search.payload;

/**
 * Represents a payload object for the fulltext search request.
 */
public interface FulltextSearchGetRequestPayload {

  String getPayload();

  boolean validate();
}
