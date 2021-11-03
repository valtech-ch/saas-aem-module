package com.valtech.aem.saas.api.query;

import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

public final class FilterFactory {

  public static Filter createFilter(@NonNull String key, @NonNull String value) {
    return new SimpleFilter(key, value);
  }

  public static Filter createFilter(@NonNull String key, @NonNull List<String> values) {
    if (CollectionUtils.isNotEmpty(values)) {
      if (values.size() > 1) {
        return CompositeFilter.builder()
            .filters(values.stream().map(v -> createFilter(key, v)).collect(Collectors.toSet())).build();
      }
      return createFilter(key, values.get(0));
    }
    return new SimpleFilter();
  }

  private FilterFactory() {
  }
}
