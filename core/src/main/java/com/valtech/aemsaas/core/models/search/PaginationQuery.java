package com.valtech.aemsaas.core.models.search;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class PaginationQuery implements FulltextSearchGetQuery {

  static final String START = "start";
  static final String ROWS = "rows";
  private static final int START_MIN_VALUE = 0;
  private static final int ROWS_MIN_VALUE = 1;

  private final NameValuePair start;
  private final NameValuePair rows;

  public PaginationQuery(int start, int rows, int rowsMaxLimit) {
    if (!isValidStartParameter(start)) {
      throw new IllegalArgumentException("Start value not in acceptable value range.");
    }
    if (!isValidRowParameter(rows, rowsMaxLimit)) {
      throw new IllegalArgumentException("Rows value not in acceptable value range.");
    }
    this.start = new BasicNameValuePair(START, String.valueOf(start));
    this.rows = new BasicNameValuePair(ROWS, String.valueOf(rows));
  }

  @Override
  public List<NameValuePair> getEntries() {
    List<NameValuePair> entries = new ArrayList<>();
    entries.add(start);
    entries.add(rows);
    return entries;
  }

  private boolean isValidStartParameter(int start) {
    return start >= START_MIN_VALUE;
  }

  private boolean isValidRowParameter(int rows, int maxLimit) {
    return rows > ROWS_MIN_VALUE && rows <= maxLimit;
  }


}
