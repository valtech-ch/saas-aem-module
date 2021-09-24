package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.Filter;
import java.util.Collection;
import java.util.List;
import java.util.StringJoiner;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;

@Builder
public class CompositeFilter implements Filter {

  public static final String PREFIX = "(";
  public static final String SUFFIX = ")";
  @Singular
  List<Filter> filters;

  @Default
  FilterJoinOperator joinOperator = FilterJoinOperator.AND;

  public static ExtendedCompositeFilterBuilder builder() {
    return new ExtendedCompositeFilterBuilder();
  }

  public static class ExtendedCompositeFilterBuilder extends CompositeFilterBuilder {

    public ExtendedCompositeFilterBuilder filter(String name, String value) {
      return filter(new SimpleFilter(name, value));
    }

    @Override
    public ExtendedCompositeFilterBuilder filter(Filter filter) {
      return (ExtendedCompositeFilterBuilder) super.filter(filter);
    }

    @Override
    public ExtendedCompositeFilterBuilder joinOperator(FilterJoinOperator joinOperator) {
      return (ExtendedCompositeFilterBuilder) super.joinOperator(joinOperator);
    }

    @Override
    public ExtendedCompositeFilterBuilder clearFilters() {
      return (ExtendedCompositeFilterBuilder) super.clearFilters();
    }

    @Override
    public ExtendedCompositeFilterBuilder filters(Collection<? extends Filter> filters) {
      return (ExtendedCompositeFilterBuilder) super.filters(filters);
    }
  }

  @Override
  public String getString() {
    StringJoiner stringJoiner = new StringJoiner(joinOperator.getStringRepresentation(), PREFIX, SUFFIX).setEmptyValue(
        StringUtils.EMPTY);
    filters.stream()
        .map(Filter::getString)
        .filter(StringUtils::isNotBlank)
        .forEach(stringJoiner::add);
    return stringJoiner.toString();
  }
}
