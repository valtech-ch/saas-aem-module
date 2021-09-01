package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanguageQueryTest {

  @Test
  void testQuery() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> new LanguageQuery(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new LanguageQuery(""));
    assertThat(new LanguageQuery("de").getEntries().size(), is(1));
  }
}
