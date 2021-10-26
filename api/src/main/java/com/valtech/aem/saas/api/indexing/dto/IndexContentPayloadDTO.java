package com.valtech.aem.saas.api.indexing.dto;

import com.google.gson.JsonObject;

/**
 * An object representing the payload for content indexing requests.
 */
public interface IndexContentPayloadDTO {

  /**
   * Gets the content that is indexed.
   *
   * @return text content.
   */
  String getContent();

  /**
   * Gets the title of the content that is indexed.
   *
   * @return title text
   */
  String getTitle();

  /**
   * Gets the url of the page which is indexed.
   *
   * @return url string.
   */
  String getUrl();

  /**
   * Gets a repository path regex matching the page location.
   *
   * @return regex text
   */
  String getRepositoryPath();

  /**
   * Gets a json object containing specific metadata for indexing.
   *
   * @return json object
   */
  JsonObject getMetadata();

}
