package com.valtech.aem.saas.core.indexing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@RequiredArgsConstructor
public enum IndexUpdateAction {
  UPDATE("update"),
  DELETE("delete");

  @Getter
  private final String name;

  public static IndexUpdateAction fromName(String name) {
    if (StringUtils.isBlank(name)) {
      return null;
    }
    try {
      return valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
      log.error("Failed to resolve enum item for name: {}.", name, e);
    }
    return null;
  }
}
