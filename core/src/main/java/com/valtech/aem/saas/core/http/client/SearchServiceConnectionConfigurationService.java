package com.valtech.aem.saas.core.http.client;

/**
 * Provides methods for retrieving configuration for the search service connection details.
 */
public interface SearchServiceConnectionConfigurationService {

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
