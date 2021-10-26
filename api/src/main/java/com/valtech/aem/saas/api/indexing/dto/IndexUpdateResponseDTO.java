package com.valtech.aem.saas.api.indexing.dto;

/**
 * An object representing the response of an index update request.
 */
public interface IndexUpdateResponseDTO {

  /**
   * Gets short feedback message for the action's success or failure.
   *
   * @return text message.
   */
  String getMessage();

  /**
   * Gets the page's url which content is indexed.
   *
   * @return page url string.
   */
  String getUrl();

  /**
   * Gets the site id.
   *
   * @return site id.
   */
  String getSiteId();

  /**
   * Gets the index id.
   *
   * @return index id.
   */
  String getId();
}
