package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultLanguageQueryTest {

  @Test
  void testQuery() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> new DefaultLanguageQuery(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new DefaultLanguageQuery(""));
    assertThat(new DefaultLanguageQuery("de").getEntries().size(), is(1));
  }
}
