package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.TypeaheadQuery;
import com.valtech.aem.saas.core.typeahead.TypeaheadSearchTextParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Default implementation of {@link TypeaheadQuery}. It forms the query from a provided text string.
 */
public class TypeaheadTextQuery implements TypeaheadQuery {

  public static final String KEY_TERM = "term";
  public static final String KEY_PREFIX = "prefix";

  private NameValuePair term;
  private NameValuePair prefix;

  public TypeaheadTextQuery(String searchText) {
    TypeaheadSearchTextParser parser = new TypeaheadSearchTextParser(searchText);
    parser.getTerm().ifPresent(t -> term = new BasicNameValuePair(KEY_TERM, t));
    parser.getPrefix().ifPresent(p -> prefix = new BasicNameValuePair(KEY_PREFIX, p));
  }

  @Override
  public List<NameValuePair> getEntries() {
    List<NameValuePair> entries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(entries, term);
    CollectionUtils.addIgnoreNull(entries, prefix);
    return entries;
  }

}
