package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.FulltextSearchOptionalQuery;
import com.valtech.aem.saas.api.query.Query;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Implementation of {@link Query} that specifies pagination parameters: "start" and "rows".
 */
@Slf4j
public final class PaginationQuery implements FulltextSearchOptionalQuery {

  static final String START_PARAMETER_KEY = "start";
  static final String ROWS_PARAMETER_KEY = "rows";
  private static final int START_MIN_VALUE = 0;
  private static final int ROWS_MIN_VALUE = 1;

  private final NameValuePair start;
  private final NameValuePair rows;

  /**
   * Constructs a pagination query with current page, results per page/request and the maximum limit.
   *
   * @param start the current result page.
   * @param rows  the number of results per page/request.
   */
  public PaginationQuery(int start, int rows) {
    if (isValidStartParameter(start)) {
      this.start = new BasicNameValuePair(START_PARAMETER_KEY, String.valueOf(start));
    } else {
      this.start = null;
      log.warn("Start value not in acceptable value range.");
    }
    if (isValidRowParameter(rows)) {
      this.rows = new BasicNameValuePair(ROWS_PARAMETER_KEY, String.valueOf(rows));
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

  private boolean isValidRowParameter(int rows) {
    return rows > ROWS_MIN_VALUE;
  }

}
