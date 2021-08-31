package com.valtech.aemsaas.core.models.search;

public final class TermQuery implements FulltextSearchGetQuery, TypeaheadGetQuery {

  static final String PARAMETER = "term";

  private final SimpleGetQuery termQuery;

  public TermQuery(String value) {
    termQuery = new SimpleGetQuery(PARAMETER, value);
  }

  @Override
  public String getString() {
    return termQuery.getString();
  }
}
