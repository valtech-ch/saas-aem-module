package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ComponentExporter;
import java.util.List;

/**
 * Represents a model of the aem search results component.
 */
public interface SearchResults extends ComponentExporter {

  /**
   * Retrieves the search term present in the adapted request.
   *
   * @return search term.
   */
  String getTerm();

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
