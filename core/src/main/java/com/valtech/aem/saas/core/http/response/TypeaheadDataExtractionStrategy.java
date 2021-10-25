package com.valtech.aem.saas.core.http.response;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.valtech.aem.saas.core.http.response.dto.FacetCountsDto;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.MapUtils;

/**
 * A strategy for extracting typeahead options.
 */
@RequiredArgsConstructor
public final class TypeaheadDataExtractionStrategy implements SearchResponseDataExtractionStrategy<List<String>> {

  private static final String TYPEAHEAD_INDEX_FIELD_NAME_PREFIX = "autocomplete_text_";

  private final String language;

  @Override
  public String propertyName() {
    return FacetCountsDto.PN_FACET_COUNTS;
  }

  @Override
  public Optional<List<String>> getData(JsonElement response) {
    return Optional.ofNullable(response)
        .filter(JsonElement::isJsonObject)
        .map(JsonElement::getAsJsonObject)
        .map(r -> r.getAsJsonObject(propertyName()))
        .map(jsonObject -> new Gson().fromJson(jsonObject, FacetCountsDto.class))
        .map(this::getTypeaheadResults);
  }

  private List<String> getTypeaheadResults(@NonNull FacetCountsDto facetCounts) {
    return Optional.ofNullable(facetCounts.getFacetFields())
        .filter(MapUtils::isNotEmpty)
        .map(map -> map.get(getTypeaheadIndexFieldName()))
        .map(this::getTypeaheadItems)
        .orElse(Collections.emptyList());
  }

  private List<String> getTypeaheadItems(@NonNull List<Object> facetFieldsList) {
    List<String> items = new ArrayList<>();
    for (int i = 0; i < facetFieldsList.size(); i++) {
      if (isTypeaheadTextOption(facetFieldsList, i)) {
        items.add((String) facetFieldsList.get(i));
      }
    }
    return items;
  }

  private boolean isTypeaheadTextOption(List<Object> facetFieldsList, int i) {
    return i % 2 == 0 && facetFieldsList.get(i) instanceof String;
  }

  private String getTypeaheadIndexFieldName() {
    return TYPEAHEAD_INDEX_FIELD_NAME_PREFIX + language;
  }
}
