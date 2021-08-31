package com.valtech.aemsaas.core.models.search;

import org.apache.commons.lang3.StringUtils;

public final class SortQuery implements FulltextSearchGetQuery {

  static final String PARAMETER = "sort";
  private static final String SORT_DELIMITER = " ";

  private final Sort sort;
  private final SimpleGetQuery sortBy;

  public SortQuery(String field, Sort sort) {
    this.sortBy = new SimpleGetQuery(PARAMETER, field);
    this.sort = sort;
  }

  public SortQuery(String field) {
    this(field, Sort.ASC);
  }

  @Override
  public String getString() {
    String sortByString = sortBy.getString();
    if (StringUtils.isNotEmpty(sortByString)) {
      return StringUtils.join(sortBy.getString(), SORT_DELIMITER, sort.name().toLowerCase());
    }
    return StringUtils.EMPTY;
  }
}
