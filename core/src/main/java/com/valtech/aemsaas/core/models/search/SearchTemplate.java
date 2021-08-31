package com.valtech.aemsaas.core.models.search;

import lombok.Getter;

public enum SearchTemplate {
  PRODS("prods"),
  NOLANG("nolang"),
  NORMAL(""),
  RELATED("related"),
  TIMED("timed");

  @Getter
  private final String tmplName;

  SearchTemplate(String tmplName) {
    this.tmplName = tmplName;
  }
}
