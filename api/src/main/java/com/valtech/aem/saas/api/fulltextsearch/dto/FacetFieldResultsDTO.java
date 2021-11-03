package com.valtech.aem.saas.api.fulltextsearch.dto;

import java.util.List;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class FacetFieldResultsDTO {

  String fieldName;
  List<FacetFieldResultDTO> items;
}
