package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ContainerExporter;

/**
 * Represents a model of the aem search redirect component.
 */
public interface SearchRedirectModel extends ContainerExporter {

  /**
   * Gets an author configured value for component's  input field's placeholder.
   *
   * @return placeholder text.
   */
  String getSearchFieldPlaceholderText();

  /**
   * Gets the autocomplete trigger threshold.
   *
   * @return min number of chars typed before triggering the autocomplete.
   */
  int getAutocompleteTriggerThreshold();

  /**
   * Gets the search configuration in a json format. In this format, the configs are used by the FE.
   *
   * @return json formatted string.
   */
  String getConfigJson();

  /**
   * Gets the search page's path which the component redirects to.
   *
   * @return page path.
   */
  String getSearchPagePath();

}
