package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.FacetCountsDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * A strategy for extracting typeahead options.
 */
@RequiredArgsConstructor
public final class TypeaheadDataExtractionStrategy implements SearchResponseDataExtractionStrategy<List<String>> {

    private static final String TYPEAHEAD_INDEX_FIELD_NAME_PREFIX = "autocomplete_text_";

    private final String language;

    @Override
    public String propertyName() {
        return FacetCountsDTO.PN_FACET_COUNTS;
    }

    @Override
    public Optional<List<String>> getData(JsonElement response) {
        return new FacetCountsDataExtractionStrategy().getData(response)
                                                      .map(this::getTypeaheadResults);
    }

    private List<String> getTypeaheadResults(@NonNull FacetCountsDTO facetCounts) {
        return Optional.ofNullable(facetCounts.getFacetFields())
                       .filter(MapUtils::isNotEmpty)
                       .map(map -> map.get(getTypeaheadIndexFieldName()))
                       .map(this::getTypeaheadItems)
                       .orElse(Collections.emptyList());
    }

    private List<String> getTypeaheadItems(@NonNull List<JsonElement> facetFieldsList) {
        List<String> items = new ArrayList<>();
        for (int i = 0; i < facetFieldsList.size(); i++) {
            if (isTypeaheadTextOption(facetFieldsList, i)) {
                items.add(facetFieldsList.get(i).getAsString());
            }
        }
        return items;
    }

    private boolean isTypeaheadTextOption(
            List<JsonElement> facetFieldsList,
            int i) {
        return i % 2 == 0 && facetFieldsList.get(i).isJsonPrimitive() && facetFieldsList.get(i).getAsJsonPrimitive()
                                                                                        .isString();
    }

    private String getTypeaheadIndexFieldName() {
        return TYPEAHEAD_INDEX_FIELD_NAME_PREFIX + language;
    }
}
