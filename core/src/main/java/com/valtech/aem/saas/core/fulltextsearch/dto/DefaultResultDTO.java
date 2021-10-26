package com.valtech.aem.saas.core.fulltextsearch.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.fulltextsearch.dto.ResultDTO;
import lombok.Builder;
import lombok.Value;

/**
 * Default implementation that offers a builder pattern based creation logic.
 */
@Value
@Builder
public class DefaultResultDTO implements ResultDTO {

  @JsonInclude(Include.NON_EMPTY)
  String url;

  @JsonInclude(Include.NON_EMPTY)
  String title;

  @JsonInclude(Include.NON_EMPTY)
  String description;

  boolean bestBet;
}
