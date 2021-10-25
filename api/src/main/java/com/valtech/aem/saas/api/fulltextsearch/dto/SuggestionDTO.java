package com.valtech.aem.saas.api.fulltextsearch.dto;

/**
 * Value object representing the search results suggestion. It exists when a misspelled search term is queried.
 */
public interface SuggestionDTO {

  /**
   * Gets the suggested text
   *
   * @return suggested correctly spelled alternative.
   */
  String getText();

  /**
   * Gets the number of occurrences of the suggested text.
   *
   * @return total number of occurrences.
   */
  int getHits();
}
