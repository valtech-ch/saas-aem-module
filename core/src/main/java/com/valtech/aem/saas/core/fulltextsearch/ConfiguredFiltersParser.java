package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FilterFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class ConfiguredFiltersParser {

  public static final String FILTER_VALUES_SEPARATOR = ",";
  private final List<FilterModel> filters;

  public Set<Filter> getFilters() {
    return getValidFilterEntries().stream().map(this::createFilterFrom).collect(Collectors.toSet());
  }

  private List<FilterModel> getValidFilterEntries() {
    return Optional.ofNullable(filters)
        .map(List::stream)
        .orElse(Stream.empty())
        .filter(this::isValidEntry)
        .collect(Collectors.toList());
  }

  private boolean isValidEntry(FilterModel filterModel) {
    return StringUtils.isNoneBlank(filterModel.getName(), filterModel.getValue());
  }

  private Filter createFilterFrom(FilterModel filterModel) {
    return FilterFactory.createFilter(filterModel.getName(), getFilterValues(filterModel));
  }

  private List<String> getFilterValues(FilterModel filterModel) {
    String[] values = StringUtils.split(filterModel.getValue(), FILTER_VALUES_SEPARATOR);
    return Arrays.stream(values).map(StringUtils::trim).collect(Collectors.toList());
  }
}
