package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class TermQueryTest {

  @Test
  void testQuery() {
    assertThrows(IllegalArgumentException.class, () -> new TermQuery(null));
    assertThrows(IllegalArgumentException.class, () -> new TermQuery(""));
    assertThat(new TermQuery("foo").getEntries().size(), is(1));
  }
}
