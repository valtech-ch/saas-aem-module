package com.valtech.aem.saas.api.fulltextsearch.dto;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FacetFieldResultDTO {

  String text;
  int count;
}
