package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ContainerExporter;
import java.util.List;
import java.util.Set;

/**
 * Represents a model of the aem search component.
 */
public interface Search extends ContainerExporter {

  /**
   * Retrieves the title configurable for the search component.
   *
   * @return configured title.
   */
  String getTitle();

  /**
   * Retrieves the search term present in the adapted request.
   *
   * @return search term.
   */
  String getTerm();

  /**
   * Retrieves a list of author configured search filter items (union of context aware and dialog config).
   *
   * @return set of filter details.
   */
  Set<Filter> getFilters();

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
   * Gets list of search prepared urls to search tab resources;
   *
   * @return list of urls.
   */
  List<String> getSearchTabs();

}
