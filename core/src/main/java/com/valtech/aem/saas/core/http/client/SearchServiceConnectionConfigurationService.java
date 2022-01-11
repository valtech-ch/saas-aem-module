package com.valtech.aem.saas.core.http.client;

/**
 * Provides methods for retrieving configuration for the search service connection details.
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
     * Gets the jwt authentication token.
     *
     * @return jwt.
     */
    String getJwtAuthenticationToken();

    /**
     * Checks whether JWT Authentication is enabled.
     *
     * @return {@code true} if enabled.
     */
    boolean isJWTAuthenticationEnabled();

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

    /**
     * Gets the max limit of connections.
     *
     * @return positive integer.
     */
    int getHttpMaxTotalConnections();

    /**
     * Gets the max limit of connections per route.
     *
     * @return positive integer.
     */
    int getHttpMaxConnectionsPerRoute();
}
