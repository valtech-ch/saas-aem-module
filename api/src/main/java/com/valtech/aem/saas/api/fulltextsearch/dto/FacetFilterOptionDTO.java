package com.valtech.aem.saas.api.fulltextsearch.dto;


import lombok.RequiredArgsConstructor;
import lombok.Value;

/**
 * Represents an object for deserializing the facet filter option details in search results.
 */
@Value
@RequiredArgsConstructor
public class FacetFilterOptionDTO {

    String value;
    int hits;
}
