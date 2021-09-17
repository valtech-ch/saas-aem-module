package com.valtech.aem.saas.api.indexing;

/**
 * An object representing the response of an index update request.
 */
public interface IndexUpdateResponse {

  String getMessage();

  String getUrl();

  String getSiteId();

  String getId();
}
