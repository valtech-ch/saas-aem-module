package com.valtech.aemsaas.core.models.search.query;

import lombok.Getter;

public enum Sort {
  ASC("asc"),
  DESC("desc");

  Sort(String text) {
    this.text = text;
  }

  @Getter
  private final String text;
}
