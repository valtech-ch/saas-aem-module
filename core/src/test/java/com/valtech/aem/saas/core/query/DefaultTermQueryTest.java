package com.valtech.aem.saas.core.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class DefaultTermQueryTest {

  @Test
  void testQuery() {
    assertThat(new DefaultTermQuery(null).getEntries().size(), is(1));
    assertThat(new DefaultTermQuery("").getEntries().size(), is(1));
    assertThat(new DefaultTermQuery("foo").getEntries().size(), is(1));
  }
}
