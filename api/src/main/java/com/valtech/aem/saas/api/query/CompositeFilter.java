package com.valtech.aem.saas.api.query;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.StringJoiner;


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
