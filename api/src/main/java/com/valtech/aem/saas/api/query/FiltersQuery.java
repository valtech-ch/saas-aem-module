package com.valtech.aem.saas.api.query;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * {@link com.valtech.aem.saas.api.query.Query} implementation that handles search filter params.
 */
@Builder
public class FiltersQuery implements OptionalQuery {

  private static final String FILTER = "filter";
  private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

  @Singular
  private final Set<FilterModel> filters;

  @Override
  public List<NameValuePair> getEntries() {
    return filters.stream()
        .filter(f -> StringUtils.isNoneBlank(f.getName(), f.getValue()))
        .map(f -> new BasicNameValuePair(FILTER,
            String.join(FILTER_FIELD_VALUE_DELIMITER, f.getName(), f.getValue())))
        .collect(Collectors.toList());
  }
}
