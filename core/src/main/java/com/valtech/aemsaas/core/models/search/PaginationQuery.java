package com.valtech.aemsaas.core.models.search;

import org.apache.commons.lang3.StringUtils;

public final class PaginationQuery implements FulltextSearchGetQuery {

  static final String START = "start";
  static final String ROWS = "rows";
  private static final int ROWS_MAX_LIMIT = 9999;
  private static final int START_MIN_VALUE = 0;
  private static final int ROWS_MIN_VALUE = 1;

  private final SimpleGetQuery start;
  private final SimpleGetQuery rows;

  public PaginationQuery(int start, int rows) {
    if (!validateStart(start)) {
      throw new IllegalArgumentException("Start value not in acceptable value range.");
    }
    if (!validateRows(rows)) {
      throw new IllegalArgumentException("Rows value not in acceptable value range.");
    }
    this.start = new SimpleGetQuery(START, String.valueOf(start));
    this.rows = new SimpleGetQuery(ROWS, String.valueOf(rows));
  }

  @Override
  public String getString() {
    String startString = start.getString();
    String rowsString = rows.getString();
    return StringUtils.join(startString, DELIMITER, rowsString);
  }

  private boolean validateStart(int start) {
    return start >= START_MIN_VALUE;
  }

  private boolean validateRows(int rows) {
    return rows > ROWS_MIN_VALUE && rows <= ROWS_MAX_LIMIT;
  }
}
