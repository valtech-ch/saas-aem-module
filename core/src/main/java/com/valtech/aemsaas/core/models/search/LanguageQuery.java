package com.valtech.aemsaas.core.models.search;

public final class LanguageQuery implements FulltextSearchGetQuery, TypeaheadGetQuery {

  static final String PARAMETER = "lang";

  private final SimpleGetQuery languageQuery;

  public LanguageQuery(String value) {
    languageQuery = new SimpleGetQuery(PARAMETER, value);
  }

  @Override
  public String getString() {
    return languageQuery.getString();
  }
}
