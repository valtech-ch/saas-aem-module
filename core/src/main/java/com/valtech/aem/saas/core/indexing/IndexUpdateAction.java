package com.valtech.aem.saas.core.indexing;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum IndexUpdateAction {
  UPDATE("update"),
  DELETE("delete");

  @Getter
  private final String name;

  public static IndexUpdateAction fromName(String n) {
    if (n == null) {
      return null;
    } else {
      try {
        return valueOf(n.toUpperCase());
      } catch (IllegalArgumentException var2) {
        return null;
      }
    }
  }
}
