package com.valtech.aemsaas.core.models.search.query;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Implementation of {@link GetQuery} that specifies a sorting according to a specific field.
 */
public final class SortQuery implements FulltextSearchOptionalGetQuery {

  static final String KEY = "sort";

  private final NameValuePair sortBy;

  /**
   * Constructs a sorting query.
   *
   * @param field field's name to sort by
   * @param sort  sorting order: ascending or descending.
   */
  public SortQuery(String field, Sort sort) {
    if (StringUtils.isBlank(field)) {
      throw new IllegalArgumentException("Sort field must not be blank.");
    }
    this.sortBy = new BasicNameValuePair(KEY, String.format("%s %s", field, sort.getText()));
  }

  public SortQuery(String field) {
    this(field, Sort.ASC);
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(sortBy);
  }
}
