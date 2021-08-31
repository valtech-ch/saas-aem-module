package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PaginationQueryTest {

  @Test
  void getString() {
    assertThat(new PaginationQuery(0, 10).getString(),
        is(String.format("%s=0&%s=10", PaginationQuery.START, PaginationQuery.ROWS)));
    assertThrows(IllegalArgumentException.class, () -> new PaginationQuery(-1, 10).getString());
    assertThrows(IllegalArgumentException.class, () -> new PaginationQuery(0, 10000).getString());
  }
}
