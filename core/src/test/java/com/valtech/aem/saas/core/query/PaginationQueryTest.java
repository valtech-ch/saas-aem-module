package com.valtech.aem.saas.core.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class PaginationQueryTest {

  @Test
  void testQuery() {
    assertThat(new PaginationQuery(0, 10).getEntries().size(),
        is(2));
    assertThat(new PaginationQuery(-1, 10).getEntries().size(), is(1));
    assertThat(new PaginationQuery(-1, 0).getEntries(), is(empty()));
  }
}
