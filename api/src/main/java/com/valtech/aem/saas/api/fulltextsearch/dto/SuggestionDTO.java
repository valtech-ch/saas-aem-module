package com.valtech.aem.saas.api.fulltextsearch.dto;

import lombok.Value;

/**
 * Value object representing the search results suggestion. It exists when a misspelled search term is queried.
 */
@Value
public class SuggestionDTO {

  String text;

  int hits;

}
