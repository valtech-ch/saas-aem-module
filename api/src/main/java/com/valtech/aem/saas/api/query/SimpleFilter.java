package com.valtech.aem.saas.api.query;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SimpleFilter implements Filter {

  private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

  private String name;
  private String value;

  @Override
  public String getQueryString() {
    if (StringUtils.isNoneBlank(name, value)) {
      return String.join(FILTER_FIELD_VALUE_DELIMITER, name, value);
    }
    return StringUtils.EMPTY;
  }
}
