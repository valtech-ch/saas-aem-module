package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class HighlightingTagQueryTest {

  @Test
  void getString() {
    assertThat(new HighlightingTagQuery(null).getString(), is(StringUtils.EMPTY));
    assertThat(new HighlightingTagQuery("").getString(), is(StringUtils.EMPTY));
    assertThat(new HighlightingTagQuery("em").getString(),
        is(String.format("%s=em&%s=em", HighlightingTagQuery.HIGHLIGHT_PRE_TAG,
            HighlightingTagQuery.HIGHLIGHT_POST_TAG)));
  }
}
