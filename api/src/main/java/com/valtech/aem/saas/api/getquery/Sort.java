package com.valtech.aem.saas.api.getquery;

import lombok.Getter;

/**
 * Represents a sort option for {@link SortQuery}.
 */
public enum Sort {
  ASC("asc"),
  DESC("desc");

  Sort(String text) {
    this.text = text;
  }

  @Getter
  private final String text;
}
