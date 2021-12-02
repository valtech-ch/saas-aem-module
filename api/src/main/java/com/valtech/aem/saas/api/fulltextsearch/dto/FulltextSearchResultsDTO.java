package com.valtech.aem.saas.api.fulltextsearch.dto;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

/**
 * Value object representing the fulltext search results data.
 */
@Builder
@Value
public class FulltextSearchResultsDTO {

    int totalResultsFound;

    int currentResultPage;

    @Singular
    List<ResultDTO> results;

    SuggestionDTO suggestion;

    @Singular
    List<FacetFieldResultsDTO> facetFieldsResults;
}
