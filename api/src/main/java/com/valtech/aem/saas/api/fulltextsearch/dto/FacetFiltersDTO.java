package com.valtech.aem.saas.api.fulltextsearch.dto;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

/**
 * A DTO containing the facet based filters' details.
 */
@Value
@RequiredArgsConstructor
public class FacetFiltersDTO {

    String queryParameterName;
    List<FacetFilterDTO> items;
}
