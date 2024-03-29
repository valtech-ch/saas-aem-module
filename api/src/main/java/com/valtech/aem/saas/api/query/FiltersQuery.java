package com.valtech.aem.saas.api.query;

import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * {@link com.valtech.aem.saas.api.query.Query} implementation that handles search filter params.
 */
@Builder
public class FiltersQuery implements Query {

    private static final String FILTER = "filter";
    private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

    @Singular
    private final Set<Filter> filters;

    @Override
    public List<NameValuePair> getEntries() {
        return filters.stream()
                      .map(Filter::getQueryString)
                      .filter(StringUtils::isNotEmpty)
                      .map(filterString -> new BasicNameValuePair(FILTER, filterString))
                      .collect(Collectors.toList());
    }
}
