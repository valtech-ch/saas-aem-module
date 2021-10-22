package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ComponentExporter;
import java.util.List;
import java.util.Set;

/**
 * Represents a model of the aem search results component.
 */
public interface SearchTab extends ComponentExporter {

  /**
   * Retrieves the title configurable for the search tab component.
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
   * Retrieves a list of author configured search filter items (union of context aware and search cmp and current search
   * tab).
   *
   * @return set of filter details.
   */
  Set<Filter> getFilters();

  /**
   * Retrieves the current results page (i.e the results offset parameter) set in the adapted request as parameter.
   *
   * @return current results page.
   */
  int getStartPage();

  /**
   * Retrieves the max results per request/page present in the adapted request or as fallback uses the author configured
   * value for the max limit of results per request/page
   *
   * @return max number of results per request.
   */
  int getResultsPerPage();

  /**
   * Retrieves the results for the specified search query (query params in request)
   *
   * @return list of results
   */
  List<Result> getResults();

  /**
   * Get search suggestion if there is one.
   *
   * @return an object containing suggestion details.
   */
  Suggestion getSuggestion();

  /**
   * Gets the label for the load more results button
   *
   * @return load more button text.
   */
  String getLoadMoreButtonText();

  /**
   * Get the total number of results found.
   *
   * @return number of results.
   */
  int getResultsTotal();

  /**
   * A boolean flag used for displaying the load more button.
   *
   * @return true if there are more results to be loaded.
   */
  boolean isShowLoadMoreButton();
}
