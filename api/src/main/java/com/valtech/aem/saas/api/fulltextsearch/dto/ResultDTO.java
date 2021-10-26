package com.valtech.aem.saas.api.fulltextsearch.dto;

/**
 * Value object representing the fulltext search results. Each result contains title and description content; and the
 * url of the page where the content is found.
 */
public interface ResultDTO {

  /**
   * Gets the url of the search result's page.
   *
   * @return url string.
   */
  String getUrl();

  /**
   * Gets the search result's title.
   *
   * @return title.
   */
  String getTitle();

  /**
   * Gets the search result's description text.
   *
   * @return title.
   */
  String getDescription();

  /**
   * Gets the best bet boolean flag.
   *
   * @return true if result is marked as best bet.
   */
  boolean isBestBet();
}
