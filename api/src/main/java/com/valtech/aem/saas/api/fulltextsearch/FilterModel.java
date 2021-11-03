package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.query.Filter;

/**
 * Represents a sling resource that contains pre-configurable search filter details.
 */
public interface FilterModel extends Filter {

  /**
   * Gets the filter field name.
   *
   * @return filter field name.
   */
  String getName();

  /**
   * Gets the filter field value.
   *
   * @return filter value
   */
  String getValue();
}
