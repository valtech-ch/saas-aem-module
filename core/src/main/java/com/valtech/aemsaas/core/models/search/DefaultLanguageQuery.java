package com.valtech.aemsaas.core.models.search;

import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public final class DefaultLanguageQuery implements LanguageQuery {

  static final String PARAMETER = "lang";

  private final NameValuePair languageQuery;

  public DefaultLanguageQuery(String value) {
    if (StringUtils.isBlank(value)) {
      throw new IllegalArgumentException("Language value must not be blank.");
    }
    languageQuery = new BasicNameValuePair(PARAMETER, value);
  }

  @Override
  public List<NameValuePair> getEntries() {
    return Collections.singletonList(languageQuery);
  }
}