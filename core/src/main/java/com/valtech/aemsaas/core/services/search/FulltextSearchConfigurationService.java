package com.valtech.aemsaas.core.services.search;

/**
 * Represents a service that gets fulltext search related OSGi configuration.
 */
public interface FulltextSearchConfigurationService {

  /**
   * Returns the configured value for the maximum limit of results per request/page.
   *
   * @return results max limit per request/page.
   */
  int getRowsMaxLimit();
}
