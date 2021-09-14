package com.valtech.aem.saas.api.bestbets;

/**
 * Represents a best bet details object
 */
public interface BestBet {

  /**
   * Gets Best bet's id
   *
   * @return identifying int value.
   */
  int getId();

  /**
   * Gets Best bet's language
   *
   * @return a string representing a language.
   */
  String getLanguage();

  /**
   * Gets Best bet's term.
   *
   * @return a string value associated with the best bet.
   */
  String getTerm();

  /**
   * Gets Best bet's url
   *
   * @return the resource's url that is associated with the best bet.
   */
  String getUrl();

  /**
   * Gets Best bet's project id
   *
   * @return identifying int value of a project.
   */
  int getProjectId();

  /**
   * Gets Best bet's saas identifier
   *
   * @return identifying int value in SaaS.
   */
  int getIdentifier();
}
