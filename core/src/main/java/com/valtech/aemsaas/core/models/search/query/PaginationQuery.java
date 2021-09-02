package com.valtech.aemsaas.core.models.search.query;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@Slf4j
public final class PaginationQuery implements FulltextSearchOptionalGetQuery {

  static final String START = "start";
  static final String ROWS = "rows";
  private static final int START_MIN_VALUE = 0;
  private static final int ROWS_MIN_VALUE = 1;

  private final NameValuePair start;
  private final NameValuePair rows;

  public PaginationQuery(int start, int rows, int rowsMaxLimit) {
    if (isValidStartParameter(start)) {
      this.start = new BasicNameValuePair(START, String.valueOf(start));
    } else {
      this.start = null;
      log.warn("Start value not in acceptable value range.");
    }
    if (isValidRowParameter(rows, rowsMaxLimit)) {
      this.rows = new BasicNameValuePair(ROWS, String.valueOf(rows));
    } else {
      this.rows = null;
      log.warn("Rows value not in acceptable value range.");
    }
  }

  @Override
  public List<NameValuePair> getEntries() {
    List<NameValuePair> entries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(entries, start);
    CollectionUtils.addIgnoreNull(entries, rows);
    return entries;
  }

  private boolean isValidStartParameter(int start) {
    return start >= START_MIN_VALUE;
  }

  private boolean isValidRowParameter(int rows, int maxLimit) {
    return rows > ROWS_MIN_VALUE && rows <= maxLimit;
  }

}
