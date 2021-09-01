package com.valtech.aemsaas.core.models.search;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class SortQuery implements FulltextSearchOptionalGetQuery {

  static final String PARAMETER = "sort";

  private final NameValuePair sortBy;

  public SortQuery(String field, Sort sort) {
    if (StringUtils.isBlank(field)) {
      throw new IllegalArgumentException("Sort field must not be blank.");
    }
    this.sortBy = new BasicNameValuePair(PARAMETER, String.format("%s %s", field, sort.name().toLowerCase()));
  }

  public SortQuery(String field) {
    this(field, Sort.ASC);
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(sortBy);
  }
}
