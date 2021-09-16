package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.TypeaheadQuery;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class TypeaheadTextQuery implements TypeaheadQuery {

  private NameValuePair term;
  private NameValuePair prefix;

  public TypeaheadTextQuery(String searchText) {
    TypeaheadSearchTextParser parser = new TypeaheadSearchTextParser(searchText);
    parser.getTerm().ifPresent(t -> term = new BasicNameValuePair("term", t));
    parser.getPrefix().ifPresent(p -> prefix = new BasicNameValuePair("prefix", p));
  }

  @Override
  public List<NameValuePair> getEntries() {
    List<NameValuePair> entries = new ArrayList<>();
    CollectionUtils.addIgnoreNull(entries, term);
    CollectionUtils.addIgnoreNull(entries, prefix);
    return entries;
  }

}
