package com.valtech.aem.saas.api.getquery;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Builder
public class FiltersQuery implements FulltextSearchOptionalGetQuery {

  private static final String FILTER = "filter";
  private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

  @Singular
  private final Map<String, String> filterEntries;

  @Override
  public List<NameValuePair> getEntries() {
    return filterEntries.entrySet().stream()
        .filter(entry -> StringUtils.isNoneBlank(entry.getKey(), entry.getValue()))
        .map(entry -> new BasicNameValuePair(FILTER,
            String.join(FILTER_FIELD_VALUE_DELIMITER, entry.getKey(), entry.getValue())))
        .collect(Collectors.toList());
  }
}
