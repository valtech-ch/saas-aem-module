package com.valtech.aemsaas.core.models.search.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SortQueryTest {

  @Test
  void testQuery() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(""));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(null, Sort.DESC));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery("", Sort.DESC));
    assertThat(new SortQuery("foo").getEntries().size(), is(1));
    assertThat(new SortQuery("foo", Sort.ASC).getEntries().size(), is(1));
    assertThat(new SortQuery("foo", Sort.DESC).getEntries().size(), is(1));
  }
}
