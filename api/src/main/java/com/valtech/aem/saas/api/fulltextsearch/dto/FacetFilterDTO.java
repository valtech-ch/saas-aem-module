package com.valtech.aem.saas.api.fulltextsearch.dto;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Represents an object for deserializing the facet filter details in search results. The data are utilized to generate
 * facet field based selection filters.
 */
@Value
@RequiredArgsConstructor
public class FacetFilterDTO {

  String filterFieldLabel;
  String filterFieldName;
  List<FacetFilterOptionDTO> filterFieldOptions;
}
