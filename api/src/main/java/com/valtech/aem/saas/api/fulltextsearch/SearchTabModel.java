package com.valtech.aem.saas.api.fulltextsearch;

import com.adobe.cq.export.json.ComponentExporter;
import com.valtech.aem.saas.api.fulltextsearch.dto.ResultDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.SuggestionDTO;
import java.util.List;

/**
 * Represents a model of the aem search results component.
 */
public interface SearchTabModel extends ComponentExporter {

  /**
   * Retrieves the title configurable for the search tab component.
   *
   * @return configured title.
   */
  String getTitle();

  /**
   * Retrieves a list of dialog configured search filter items.
   *
   * @return list of filter details.
   */
  List<FilterModel> getFilters();

  /**
   * Retrieves the results for the specified search query (query params in request)
   *
   * @return list of results
   */
  List<ResultDTO> getResults();

  /**
   * Get search suggestion if there is one.
   *
   * @return an object containing suggestion details.
   */
  SuggestionDTO getSuggestion();

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

  /**
   * Retrieves the json export url.
   *
   * @return
   */
  String getUrl();

}
