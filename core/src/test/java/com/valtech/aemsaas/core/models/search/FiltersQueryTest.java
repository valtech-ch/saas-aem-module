package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class FiltersQueryTest {

  @Test
  void testQuery() {
    assertThat(FiltersQuery.builder().build().getEntries(), is(empty()));
    assertThat(FiltersQuery.builder()
        .filterEntry("foo", "")
        .filterEntry("bar", null).build()
        .getEntries(), is(empty()));
    assertThat(FiltersQuery.builder()
        .filterEntry("foo", "FOO")
        .filterEntry("bar", "BAR").build()
        .getEntries().size(), is(2));
    assertThat(FiltersQuery.builder()
        .filterEntry("foo", "")
        .filterEntry("bar", null)
        .filterEntry("baz", "BAZ").build()
        .getEntries().size(), is(1));
  }
}
