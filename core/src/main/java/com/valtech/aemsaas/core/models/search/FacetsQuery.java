package com.valtech.aemsaas.core.models.search;

import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;

@Builder
public class FacetsQuery implements FulltextSearchGetQuery {

  static final String FACETFIELD = "facetfield";

  @Singular
  private final List<String> fields;

  @Override
  public String getString() {
    return fields.stream()
        .filter(StringUtils::isNotBlank)
        .map(this::getFacetEntry)
        .collect(Collectors.joining(DELIMITER));
  }

  private String getFacetEntry(String field) {
    return StringUtils.join(FACETFIELD, EQUALS, field);
  }
}
