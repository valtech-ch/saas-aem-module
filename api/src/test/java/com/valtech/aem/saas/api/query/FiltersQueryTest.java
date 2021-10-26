package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class FiltersQueryTest {

  @Test
  void testQuery() {
    assertThat(FiltersQuery.builder().build().getEntries(), is(empty()));
    assertThat(FiltersQuery.builder()
        .filter(new MockFilterModel("foo", ""))
        .filter(new MockFilterModel("bar", null)).build()
        .getEntries(), is(empty()));
    assertThat(FiltersQuery.builder()
        .filter(new MockFilterModel("foo", "FOO"))
        .filter(new MockFilterModel("bar", "BAR")).build()
        .getEntries().size(), is(2));
    assertThat(FiltersQuery.builder()
        .filter(new MockFilterModel("foo", ""))
        .filter(new MockFilterModel("bar", null))
        .filter(new MockFilterModel("baz", "BAZ")).build()
        .getEntries().size(), is(1));
  }
}
