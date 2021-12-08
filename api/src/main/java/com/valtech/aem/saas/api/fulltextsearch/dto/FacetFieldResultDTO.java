package com.valtech.aem.saas.api.fulltextsearch.dto;

import lombok.Builder;
import lombok.Value;

/**
 * Represents a value object containing facet field result details.
 */
@Builder
@Value
public class FacetFieldResultDTO {

    String text;
    int count;
}
