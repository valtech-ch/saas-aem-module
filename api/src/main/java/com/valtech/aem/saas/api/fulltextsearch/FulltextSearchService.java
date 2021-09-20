package com.valtech.aem.saas.api.fulltextsearch;

/**
 *
 */

/**
 * Represents a service that performs fulltext search queries and retrieves the according results.
 *
 * @param <T> the configuration object type
 */
public interface FulltextSearchService<T> {

  /**
   * Returns the fulltext search consumer object that provides actions for executing fulltext search, for a specified
   * SaaS index.
   *
   * @param index         name of the index in SaaS.
   * @param configuration the object that will apply configuration actions.
   * @return fulltext search consumer object
   */
  FulltextSearchConsumerService getFulltextSearchConsumerService(String index, T configuration);

}
