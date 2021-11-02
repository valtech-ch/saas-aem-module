package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class TermQueryTest {

  @Test
  void testQuery() {
    assertThat(new TermQuery(null).getEntries().size(), is(1));
    assertThat(new TermQuery("").getEntries().size(), is(1));
    assertThat(new TermQuery("foo").getEntries().size(), is(1));
  }
}
