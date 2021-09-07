package com.valtech.aem.saas.api.getquery;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * A default implementation for term query. It uses "term" as parameter key.
 */
public final class DefaultTermQuery implements TermQuery {

  private static final String SEARCH_TERM_ALL = "*";
  private static final String KEY = "term";

  private final NameValuePair termQuery;

  /**
   * Constructs a search term query.
   * @param value the search term.
   */
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
