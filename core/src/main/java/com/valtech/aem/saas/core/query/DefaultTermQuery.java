package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.TermQuery;
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

  private final NameValuePair term;

  /**
   * Constructs a search term query.
   *
   * @param value the search term.
   */
  public DefaultTermQuery(String value) {
    term = new BasicNameValuePair(KEY, getSafeTerm(value));
  }

  public DefaultTermQuery() {
    this(SEARCH_TERM_ALL);
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(term);
  }

  private String getSafeTerm(String term) {
    return StringUtils.isNotBlank(term) ? term : SEARCH_TERM_ALL;
  }
}
