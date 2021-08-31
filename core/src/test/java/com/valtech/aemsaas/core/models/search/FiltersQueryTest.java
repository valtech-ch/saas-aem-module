package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class FiltersQueryTest {

  @Test
  void getString() {
    assertThat(FiltersQuery.builder().build().getString(), is(StringUtils.EMPTY));
    assertThat(FiltersQuery.builder()
        .filterEntry("foo", "")
        .filterEntry("bar", null).build()
        .getString(), is(StringUtils.EMPTY));
    assertThat(FiltersQuery.builder()
        .filterEntry("foo", "FOO")
        .filterEntry("bar", "BAR").build()
        .getString(), is(String.format("%s=foo:FOO&%s=bar:BAR", FiltersQuery.FILTER, FiltersQuery.FILTER)));
    assertThat(FiltersQuery.builder()
        .filterEntry("foo", "")
        .filterEntry("bar", null)
        .filterEntry("baz", "BAZ").build()
        .getString(), is(String.format("%s=baz:BAZ", FiltersQuery.FILTER)));
  }
}
