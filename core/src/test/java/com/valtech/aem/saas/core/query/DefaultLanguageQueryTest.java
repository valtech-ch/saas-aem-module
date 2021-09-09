package com.valtech.aem.saas.core.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DefaultLanguageQueryTest {

  @Test
  void testQuery() {
    assertThrows(IllegalArgumentException.class, () -> new DefaultLanguageQuery(null));
    assertThrows(IllegalArgumentException.class, () -> new DefaultLanguageQuery(""));
    assertThat(new DefaultLanguageQuery("de").getEntries().size(), is(1));
  }
}
