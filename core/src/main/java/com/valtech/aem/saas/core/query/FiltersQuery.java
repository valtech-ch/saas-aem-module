package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FulltextSearchOptionalQuery;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Builder
public class FiltersQuery implements FulltextSearchOptionalQuery {

  private static final String FILTER = "filter";

  @Singular
  private final List<Filter> filters;

  public static ExtendedFiltersQueryBuilder builder() {
    return new ExtendedFiltersQueryBuilder();
  }

  public static class ExtendedFiltersQueryBuilder extends FiltersQueryBuilder {

    public ExtendedFiltersQueryBuilder filter(String name, String value) {
      return filter(new SimpleFilter(name, value));
    }

    @Override
    public ExtendedFiltersQueryBuilder filter(Filter filter) {
      return (ExtendedFiltersQueryBuilder) super.filter(filter);
    }

    @Override
    public ExtendedFiltersQueryBuilder filters(Collection<? extends Filter> filters) {
      return (ExtendedFiltersQueryBuilder) super.filters(filters);
    }

    @Override
    public ExtendedFiltersQueryBuilder clearFilters() {
      return (ExtendedFiltersQueryBuilder) super.clearFilters();
    }
  }

  @Override
  public List<NameValuePair> getEntries() {
    return filters.stream()
        .map(Filter::getString)
        .filter(StringUtils::isNotEmpty)
        .map(filterString -> new BasicNameValuePair(FILTER, filterString))
        .collect(Collectors.toList());
  }
}
