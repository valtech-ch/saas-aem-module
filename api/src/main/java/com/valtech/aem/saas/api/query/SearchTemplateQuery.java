package com.valtech.aem.saas.api.query;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Implementation of {@link Query} that specifies an admin configured search template (a set of fields with arbitrary
 * boost values).
 */
public final class SearchTemplateQuery implements OptionalQuery {

  private static final String DEFAULT_NAME = "tmpl";

  private final NameValuePair searchTemplate;

  /**
   * Constructs a search template query.
   *
   * @param searchTemplate search template name.
   */
  public SearchTemplateQuery(String searchTemplate) {
    if (StringUtils.isBlank(searchTemplate)) {
      throw new IllegalArgumentException("Must specify template value.");
    }
    this.searchTemplate = new BasicNameValuePair(DEFAULT_NAME, searchTemplate);
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(searchTemplate);
  }
}
