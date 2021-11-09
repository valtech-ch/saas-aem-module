package com.valtech.aem.saas.api.fulltextsearch;

/**
 * Represents a sling resource that contains pre-configurable search filter details.
 */
public interface FilterModel {

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
