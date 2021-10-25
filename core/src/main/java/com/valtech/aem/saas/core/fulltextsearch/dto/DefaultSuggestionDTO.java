package com.valtech.aem.saas.core.fulltextsearch.dto;

import com.valtech.aem.saas.api.fulltextsearch.dto.SuggestionDTO;
import lombok.Value;

/**
 * Represent search suggestion details.
 */
@Value
public class DefaultSuggestionDTO implements SuggestionDTO {

  String text;

  int hits;

}
