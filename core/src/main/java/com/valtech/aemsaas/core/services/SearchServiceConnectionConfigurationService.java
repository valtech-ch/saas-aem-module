package com.valtech.aemsaas.core.services;

/**
 * Provides methods for retrieving configuration for the search service details
 */
public interface SearchServiceConnectionConfigurationService {

  /**
   * Gets the base url/domain of the search api.
   *
   * @return
   */
  String getBaseUrl();

  /**
   * Gets the username for basic authentication.
   *
   * @return username.
   */
  String getBasicAuthenticationUser();

  /**
   * Gets the password for basic authentication.
   *
   * @return password.
   */
  String getBasicAuthenticationPassword();

  /**
   * Checks whether Basic Authentication is enabled.
   *
   * @return {@code true} if enabled.
   */
  boolean isBasicAuthenticationEnabled();

  /**
   * Checks whether Ignore SSL is enabled.
   *
   * @return {@code true} if enabled.
   */
  boolean isIgnoreSslEnabled();

  /**
   * Gets request connection timeout value.
   *
   * @return timeout in seconds.
   */
  int getHttpConnectionTimeout();

  /**
   * Gets socket timeout value.
   *
   * @return timeout in seconds.
   */
  int getHttpSocketTimeout();
}
