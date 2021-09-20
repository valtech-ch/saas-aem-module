package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ContainerExporter;
import java.util.List;

/**
 * Represents a model of the aem search component.
 */
public interface Search extends ContainerExporter {

  /**
   * Retrieves the search term present in the adapted request.
   *
   * @return search term.
   */
  String getTerm();

  /**
   * Retrieves a list of author configured search filter items.
   *
   * @return list of filter details.
   */
  List<Filter> getFilters();

  /**
   * Retrieves an author configured value for the max limit of results per request/page
   *
   * @return max number of results per request.
   */
  int getResultsPerPage();

  /**
   * Gets an author configured value for search input field's placeholder.
   *
   * @return placeholder text.
   */
  String getSearchFieldPlaceholderText();

  /**
   * Gets the label for the search button.
   *
   * @return search button text.
   */
  String getSearchButtonText();

  /**
   * Gets the label for the load more results button
   *
   * @return load more button text.
   */
  String getLoadMoreButtonText();

  /**
   * Gets the autocomplete trigger threshold.
   *
   * @return min number of chars typed before trigerring the autocomplete.
   */
  int getAutocompleteTriggerThreshold();

  /**
   * Checks whether best bets feature is enabled.
   *
   * @return true when enabled.
   */
  boolean isBestBetsEnabled();

  /**
   * Checks whether autosuggest feature is enabled.
   *
   * @return true when enabled.
   */
  boolean isAutoSuggestEnabled();
}
