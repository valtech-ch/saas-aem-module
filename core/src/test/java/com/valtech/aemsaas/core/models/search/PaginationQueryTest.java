package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PaginationQueryTest {

  @Test
  void testQuery() {
    assertThat(new PaginationQuery(0, 10, 100).getEntries().size(),
        is(2));
    assertThrows(IllegalArgumentException.class, () -> new PaginationQuery(-1, 10, 100));
    assertThrows(IllegalArgumentException.class, () -> new PaginationQuery(0, 10000, 9999));
  }
}
