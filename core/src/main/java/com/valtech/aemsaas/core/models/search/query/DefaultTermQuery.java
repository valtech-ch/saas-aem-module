package com.valtech.aemsaas.core.models.search.query;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * A default implementation for term query.
 */
public final class DefaultTermQuery implements TermQuery {

  private static final String SEARCH_TERM_ALL = "*";
  private static final String KEY = "term";

  private final NameValuePair termQuery;

  public DefaultTermQuery(String value) {
    termQuery = new BasicNameValuePair(KEY, getSafeTerm(value));
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(termQuery);
  }

  private String getSafeTerm(String term) {
    return StringUtils.isNotBlank(term) ? term : SEARCH_TERM_ALL;
  }
}
