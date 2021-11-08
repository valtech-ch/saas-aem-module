package com.valtech.aem.saas.api.query;

import java.util.Collection;
import java.util.Set;
import java.util.StringJoiner;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;


/**
 * Implementation of Filter that can create composite filter query entries. A composite filter represents a list of
 * filters that are joined by an 'AND' or 'OR' operator.
 */
@EqualsAndHashCode
@Builder
public class CompositeFilter implements Filter {

  public static final String PREFIX = "(";
  public static final String SUFFIX = ")";

  @Singular
  Set<Filter> filters;

  @Default
  FilterJoinOperator joinOperator = FilterJoinOperator.OR;

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
  public String getQueryString() {
    StringJoiner stringJoiner = new StringJoiner(joinOperator.getText(), PREFIX, SUFFIX).setEmptyValue(
        StringUtils.EMPTY);
    filters.stream()
        .map(Filter::getQueryString)
        .filter(StringUtils::isNotBlank)
        .forEach(stringJoiner::add);
    return stringJoiner.toString();
  }
}
