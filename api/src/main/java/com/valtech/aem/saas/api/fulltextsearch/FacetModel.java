package com.valtech.aem.saas.api.fulltextsearch;

/**
 * Represents a sling resource that contains pre-configurable search facet fields.
 */
public interface FacetModel {

  /**
   * Retrieves the label associated to the field. Label is used as a human-readable text.
   *
   * @return field label
   */
  String getLabel();

  /**
   * Retrieves the facet field's name.
   *
   * @return field name
   */
  String getFieldName();

}
