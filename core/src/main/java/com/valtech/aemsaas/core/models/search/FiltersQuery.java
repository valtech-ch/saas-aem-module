package com.valtech.aemsaas.core.models.search;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;

@Builder
public class FiltersQuery implements FulltextSearchGetQuery {

  static final String FILTER = "filter";
  private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

  @Singular
  private final Map<String, String> filterEntries;

  @Override
  public String getString() {
    return filterEntries.entrySet().stream()
        .filter(e -> StringUtils.isNoneBlank(e.getKey(), e.getValue()))
        .map(e -> getFilterEntry(e.getKey(), e.getValue()))
        .collect(Collectors.joining(DELIMITER));
  }

  private String getFilterEntry(String field, String value) {
    return StringUtils.join(FILTER, EQUALS, field, FILTER_FIELD_VALUE_DELIMITER, value);
  }
}
