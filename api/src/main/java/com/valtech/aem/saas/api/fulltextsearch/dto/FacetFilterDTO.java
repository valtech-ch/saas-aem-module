package com.valtech.aem.saas.api.fulltextsearch.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@RequiredArgsConstructor
public class FacetFilterDTO {

  String filterFieldLabel;
  String filterFieldName;
  List<FacetFilterOptionDTO> filterFieldOptions;
}
