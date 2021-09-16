package com.valtech.aem.saas.api.typeahead;

import java.util.List;

/**
 * Exposes a typeahead service osgi configurations.
 */
public interface TypeaheadConfigurationService {

  /**
   * Gets a list of allowed filter field names for typeahead query.
   *
   * @return list of filter field names.
   */
  List<String> getAllowedFilterFields();

}
