package com.valtech.aem.saas.api.fulltextsearch.dto;

import java.util.List;

/**
 * Value object representing the fulltext search results data.
 */
public interface FulltextSearchResultsDTO {

  /**
   * Gets the total number of search results.
   *
   * @return results total
   */
  int getTotalResultsFound();

  /**
   * Gets the current result page
   *
   * @return the current page of search results pagination.
   */
  int getCurrentResultPage();

  /**
   * Gets the search results for the current page.
   *
   * @return list of result objects.
   */
  List<ResultDTO> getResults();

  /**
   * Gets the suggestion details.
   *
   * @return object containing the suggestion text and the number of hits.
   */
  SuggestionDTO getSuggestion();
}
