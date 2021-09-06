package com.valtech.aemsaas.core.models.search.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Result {

  @JsonInclude(Include.NON_EMPTY)
  String url;

  @JsonInclude(Include.NON_EMPTY)
  @JsonRawValue
  String title;

  @JsonInclude(Include.NON_EMPTY)
  @JsonRawValue
  String description;
}
