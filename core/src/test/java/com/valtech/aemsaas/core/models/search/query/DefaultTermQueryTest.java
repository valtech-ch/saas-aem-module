package com.valtech.aemsaas.core.models.search.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.valtech.aemsaas.core.models.search.query.DefaultTermQuery;
import org.junit.jupiter.api.Test;

class DefaultTermQueryTest {

  @Test
  void testQuery() {
    assertThat(new DefaultTermQuery(null).getEntries().size(), is(1));
    assertThat(new DefaultTermQuery("").getEntries().size(), is(1));
    assertThat(new DefaultTermQuery("foo").getEntries().size(), is(1));
  }
}
