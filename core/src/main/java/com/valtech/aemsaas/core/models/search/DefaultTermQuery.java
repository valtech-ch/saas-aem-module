package com.valtech.aemsaas.core.models.search;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class DefaultTermQuery implements TermQuery {

  private static final String SEARCH_TERM_ALL = "*";

  static final String PARAMETER = "term";

  private final NameValuePair termQuery;

  public DefaultTermQuery(String value) {
    termQuery = new BasicNameValuePair(PARAMETER, getSafeTerm(value));
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(termQuery);
  }

  private String getSafeTerm(String term) {
    return StringUtils.isNotBlank(term) ? term : SEARCH_TERM_ALL;
  }
}
