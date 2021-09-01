package com.valtech.aemsaas.core.models.search;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class TermQuery implements FulltextSearchGetQuery, TypeaheadGetQuery {

  static final String PARAMETER = "term";

  private final NameValuePair termQuery;

  public TermQuery(String value) {
    if (StringUtils.isEmpty(value)) {
      throw new IllegalArgumentException("Term must not be empty.");
    }
    termQuery = new BasicNameValuePair(PARAMETER, value);
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(termQuery);
  }
}
