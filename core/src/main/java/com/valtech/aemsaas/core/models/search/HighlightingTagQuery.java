package com.valtech.aemsaas.core.models.search;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public class HighlightingTagQuery implements FulltextSearchGetQuery {

  static final String HIGHLIGHT_PRE_TAG = "hlpre";
  static final String HIGHLIGHT_POST_TAG = "hlpost";

  private final String tagName;

  @Override
  public String getString() {
    if (StringUtils.isNotBlank(tagName)) {
      return StringUtils.join(HIGHLIGHT_PRE_TAG, EQUALS, tagName, DELIMITER, HIGHLIGHT_POST_TAG, EQUALS, tagName);
    }
    return StringUtils.EMPTY;
  }

}
