package com.valtech.aem.saas.api.query;

import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * It forms the typeahead query from a provided text string.
 */
public class TypeaheadTextQuery implements Query {

  public static final String SEARCH_TEXT_DELIMITER = " ";
  public static final String REGEX_MATCHING_CONSECUTIVE_WHITESPACE_CHARS = "\\s{2,}";

  public static final String KEY_TERM = "term";
  public static final String KEY_PREFIX = "prefix";
  public static final String SEARCH_ALL = "*";

  private NameValuePair term;
  private NameValuePair prefix;

  public TypeaheadTextQuery(String searchText) {
    if (StringUtils.isNotBlank(searchText)) {
      TypeaheadSearchTextParser parser = new TypeaheadSearchTextParser(sanitizeSearchText(searchText));
      parser.getTerm().ifPresent(t -> {
        term = new BasicNameValuePair(KEY_TERM, t + SEARCH_ALL);
        prefix = new BasicNameValuePair(KEY_PREFIX, parser.getPrefix().orElse(StringUtils.EMPTY));
      });
    }
  }

  @Override
  public List<NameValuePair> getEntries() {
    List<NameValuePair> entries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(entries, term);
    CollectionUtils.addIgnoreNull(entries, prefix);
    return entries;
  }

  private String sanitizeSearchText(String searchText) {
    return StringUtils.trim(trimConsecutiveWhitespaceChars(searchText));
  }


  private String trimConsecutiveWhitespaceChars(@NonNull String text) {
    return text.replaceAll(REGEX_MATCHING_CONSECUTIVE_WHITESPACE_CHARS, SEARCH_TEXT_DELIMITER);
  }

}
