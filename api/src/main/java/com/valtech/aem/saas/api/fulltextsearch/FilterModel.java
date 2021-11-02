package com.valtech.aem.saas.api.fulltextsearch;

import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Represents a sling resource that contains pre-configurable search filter details.
 */
public interface FilterModel {

  /**
   * Gets the filter field name.
   *
   * @return filter field name.
   */
  @ValueMapValue
  String getName();

  /**
   * Gets the filter field value.
   *
   * @return filter value
   */
  @ValueMapValue
  String getValue();
}
