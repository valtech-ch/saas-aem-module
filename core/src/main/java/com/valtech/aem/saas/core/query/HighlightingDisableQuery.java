package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.FulltextSearchOptionalQuery;
import com.valtech.aem.saas.api.query.Query;
import java.util.Collections;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

/**
 * Implementation of {@link Query} that, when used, disables the highlighting feature.
 */
public final class HighlightingDisableQuery implements FulltextSearchOptionalQuery {

  private static final String HIGHLIGHT_SWITCH = "hl";

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(new BasicNameValuePair(HIGHLIGHT_SWITCH, Boolean.FALSE.toString()));
  }
}
