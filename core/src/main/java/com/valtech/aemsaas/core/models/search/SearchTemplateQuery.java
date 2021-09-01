package com.valtech.aemsaas.core.models.search;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class SearchTemplateQuery implements FulltextSearchOptionalGetQuery, TypeaheadGetQuery {

  private static final String DEFAULT_NAME = "tmpl";

  private final NameValuePair searchTemplateQuery;

  public SearchTemplateQuery(String searchTemplate) {
    if (StringUtils.isBlank(searchTemplate)) {
      throw new IllegalArgumentException("Must specify template value.");
    }
    this.searchTemplateQuery = new BasicNameValuePair(DEFAULT_NAME, searchTemplate);
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(searchTemplateQuery);
  }
}
