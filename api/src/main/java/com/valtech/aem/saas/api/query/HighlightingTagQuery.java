package com.valtech.aem.saas.api.query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

@RequiredArgsConstructor
public class HighlightingTagQuery implements OptionalQuery {

  static final String HIGHLIGHT_PRE_TAG = "hlpre";
  static final String HIGHLIGHT_POST_TAG = "hlpost";

  private final String tagName;

  @Override
  public List<NameValuePair> getEntries() {
    return Optional.ofNullable(tagName).filter(StringUtils::isNotEmpty).map(tag -> {
      List<NameValuePair> entries = new ArrayList<>();
      entries.add(new BasicNameValuePair(HIGHLIGHT_PRE_TAG, String.format("<%s>", tag)));
      entries.add(new BasicNameValuePair(HIGHLIGHT_POST_TAG, String.format("</%s>", tag)));
      return entries;
    }).orElse(
        Collections.emptyList());
  }
}
