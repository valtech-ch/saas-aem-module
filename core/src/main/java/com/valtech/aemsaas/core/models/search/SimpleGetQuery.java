package com.valtech.aemsaas.core.models.search;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public final class SimpleGetQuery implements GetQuery {

  private final String name;
  private final String value;

  public String getString() {
    return StringUtils.isNoneBlank(name, value) ? StringUtils.join(name, GetQuery.EQUALS, value) : StringUtils.EMPTY;
  }
}
