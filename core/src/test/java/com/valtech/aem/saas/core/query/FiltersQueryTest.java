package com.valtech.aem.saas.core.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

import com.valtech.aem.saas.core.fulltextsearch.FilterImpl;
import org.junit.jupiter.api.Test;

class FiltersQueryTest {

  @Test
  void testQuery() {
    assertThat(FiltersQuery.builder().build().getEntries(), is(empty()));
    assertThat(FiltersQuery.builder()
        .filter(new FilterImpl("foo", ""))
        .filter(new FilterImpl("bar", null)).build()
        .getEntries(), is(empty()));
    assertThat(FiltersQuery.builder()
        .filter(new FilterImpl("foo", "FOO"))
        .filter(new FilterImpl("bar", "BAR")).build()
        .getEntries().size(), is(2));
    assertThat(FiltersQuery.builder()
        .filter(new FilterImpl("foo", ""))
        .filter(new FilterImpl("bar", null))
        .filter(new FilterImpl("baz", "BAZ")).build()
        .getEntries().size(), is(1));
  }
}
