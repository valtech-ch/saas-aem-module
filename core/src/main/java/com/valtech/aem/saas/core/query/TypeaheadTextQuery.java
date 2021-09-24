package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.TypeaheadQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Default implementation of {@link TypeaheadQuery}. It forms the query from a provided text string.
 */
public class TypeaheadTextQuery implements TypeaheadQuery {

  public static final String SEARCH_TEXT_DELIMITER = " ";
  public static final String REGEX_MATCHING_CONSECUTIVE_WHITESPACE_CHARS = "\\s{2,}";

  public static final String KEY_TERM = "term";
  public static final String KEY_PREFIX = "prefix";

  private final NameValuePair term;
  private NameValuePair prefix;

  public TypeaheadTextQuery(String searchText) {
    term = new BasicNameValuePair(KEY_TERM, "*");
    Optional.ofNullable(searchText)
        .filter(StringUtils::isNotBlank)
        .ifPresent(t -> prefix = new BasicNameValuePair(KEY_PREFIX, sanitizeSearchText(t)));
  }


  @Override
  public List<NameValuePair> getEntries() {
    if (prefix != null) {
      return Arrays.asList(term, prefix);
    }
    return Collections.emptyList();
  }

  private String sanitizeSearchText(String searchText) {
    return StringUtils.trim(trimConsecutiveWhitespaceChars(searchText));
  }

  private String trimConsecutiveWhitespaceChars(String text) {
    return text.replaceAll(REGEX_MATCHING_CONSECUTIVE_WHITESPACE_CHARS, SEARCH_TEXT_DELIMITER);
  }

}
