package com.valtech.aem.saas.api.fulltextsearch.dto;

import lombok.Builder;
import lombok.Value;

import java.util.List;

/**
 * Represents a value object containing facet field results details.
 */
@Builder
@Value
public class FacetFieldResultsDTO {

    String fieldName;
    List<FacetFieldResultDTO> items;
}
