package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonElement;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFieldResultDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFieldResultsDTO;
import com.valtech.aem.saas.core.http.response.dto.FacetCountsDTO;
import lombok.NonNull;
import org.apache.commons.collections4.MapUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A strategy for extracting response body data.
 */
public final class FacetFieldsDataExtractionStrategy implements
        SearchResponseDataExtractionStrategy<List<FacetFieldResultsDTO>> {

    @Override
    public String propertyName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<List<FacetFieldResultsDTO>> getData(JsonElement response) {
        return new FacetCountsDataExtractionStrategy().getData(response)
                                                      .map(this::getFacetResults);
    }

    private List<FacetFieldResultsDTO> getFacetResults(@NonNull FacetCountsDTO facetCounts) {
        return Optional.ofNullable(facetCounts.getFacetFields())
                       .filter(MapUtils::isNotEmpty)
                       .map(HashMap::entrySet)
                       .map(Collection::stream)
                       .orElse(Stream.empty())
                       .map(entry -> FacetFieldResultsDTO.builder().fieldName(entry.getKey())
                                                         .items(getFacetFieldItems(entry.getValue())).build())
                       .collect(Collectors.toList());
    }

    private List<FacetFieldResultDTO> getFacetFieldItems(@NonNull List<JsonElement> facetFieldsList) {
        List<FacetFieldResultDTO> items = new ArrayList<>();
        //due to the specific way the facet field result items are delivered.(flat list of text, count pairs)
        //they list is iterated in pairs
        //i - is the index of the facet value
        //i + 1 - is the number of hits for the according value
        for (int i = 0; i < facetFieldsList.size(); i = i + 2) {
            items.add(FacetFieldResultDTO.builder()
                                         .text(facetFieldsList.get(i).getAsString())
                                         .count(facetFieldsList.get(i + 1).getAsInt())
                                         .build());
        }
        return items;
    }
}
