package com.valtech.aem.saas.api.typeahead;

/**
 * Represents a service that consumes the SaaS typeahead api.
 */
public interface TypeaheadService {

  /**
   * Gets a typeahead consumer service for a specific index.
   *
   * @param index SaaS client index.
   * @return typeahead consumer object.
   * @throws IllegalArgumentException exception thrown when blank index argument is passed.
   */
  TypeaheadConsumerService getTypeaheadConsumerService(String index);

}
