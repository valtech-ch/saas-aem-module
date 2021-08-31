package com.valtech.aemsaas.core.models.search;

import org.apache.commons.lang3.StringUtils;

public final class HighlightingDisableQuery implements FulltextSearchGetQuery {

  static final String HIGHLIGHT_SWITCH = "hl";

  @Override
  public String getString() {
    return StringUtils.join(HIGHLIGHT_SWITCH, EQUALS, Boolean.FALSE);
  }
}
