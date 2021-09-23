package com.valtech.aem.saas.core.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DefaultTermQueryTest {

  @Test
  void testQuery() {
    assertThrows(IllegalArgumentException.class, () -> new DefaultTermQuery(null));
    assertThrows(IllegalArgumentException.class, () -> new DefaultTermQuery(""));
    assertThat(new DefaultTermQuery("foo").getEntries().size(), is(1));
  }
}
