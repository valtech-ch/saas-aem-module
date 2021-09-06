package com.valtech.aemsaas.core.models.search.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.valtech.aemsaas.core.models.search.query.HighlightingDisableQuery;
import org.junit.jupiter.api.Test;

class HighlightingDisableQueryTest {

  @Test
  void testQuery() {
    assertThat(new HighlightingDisableQuery().getEntries().size(),
        is(1));
  }
}
