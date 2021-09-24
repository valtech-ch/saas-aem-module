package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.Filter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class SimpleFilter implements Filter {

  private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

  private final String name;
  private final String value;

  @Override
  public String getString() {
    if (StringUtils.isNoneBlank(name, value)) {
      return String.join(FILTER_FIELD_VALUE_DELIMITER, name, value);
    }
    return StringUtils.EMPTY;
  }
}
