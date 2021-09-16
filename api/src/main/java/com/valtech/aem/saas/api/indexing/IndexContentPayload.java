package com.valtech.aem.saas.api.indexing;

import com.google.gson.JsonObject;

/**
 * An object representing the payload for indexing content requests.
 */
public interface IndexContentPayload {

  String getContent();

  String getTitle();

  String getUrl();

  String getRepositoryPath();

  JsonObject getMetadata();

}
