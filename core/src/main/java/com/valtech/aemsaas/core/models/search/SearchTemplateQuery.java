package com.valtech.aemsaas.core.models.search;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class SearchTemplateQuery implements FulltextSearchGetQuery, TypeaheadGetQuery {

  private static final String DEFAULT_NAME = "tmpl";

  private final SimpleGetQuery searchTemplateQuery;

  public SearchTemplateQuery(SearchTemplate searchTemplate) {
    this.searchTemplateQuery = new SimpleGetQuery(DEFAULT_NAME, searchTemplate.getTmplName());
  }

  @Override
  public String getString() {
    return searchTemplateQuery.getString();
  }
}
