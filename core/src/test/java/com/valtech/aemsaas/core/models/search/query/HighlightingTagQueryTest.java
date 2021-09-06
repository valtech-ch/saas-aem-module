package com.valtech.aemsaas.core.models.search.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import com.valtech.aemsaas.core.models.search.query.HighlightingTagQuery;
import org.junit.jupiter.api.Test;

class HighlightingTagQueryTest {

  @Test
  void testQuery() {
    assertThat(new HighlightingTagQuery(null).getEntries(), is(empty()));
    assertThat(new HighlightingTagQuery("").getEntries(), is(empty()));
    assertThat(new HighlightingTagQuery("em").getEntries().size(),
        is(2));
  }
}
