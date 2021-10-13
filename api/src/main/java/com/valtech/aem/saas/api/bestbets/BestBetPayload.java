package com.valtech.aem.saas.api.bestbets;

/**
 * Represents the payload object for best bets api
 */
public interface BestBetPayload {

  /**
   * Gets best bet's index.
   *
   * @return saas index that the best bet entry is associated with.
   */
  String getIndex();

  /**
   * Gets best bet's term.
   *
   * @return the search term that best bet entry is associated with.
   */
  String getTerm();

  /**
   * Gets best bet's language.
   *
   * @return the language that best bet entry is associated with.
   */
  String getLanguage();

  /**
   * Gets best bet's utl.
   *
   * @return the url that best bet entry is associated with.
   */
  String getUrl();

}
