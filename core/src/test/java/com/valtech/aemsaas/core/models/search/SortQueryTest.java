package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class SortQueryTest {

  @Test
  void getString() {
    assertThat(new SortQuery(null).getString(), is(StringUtils.EMPTY));
    assertThat(new SortQuery(null, Sort.DESC).getString(), is(StringUtils.EMPTY));
    assertThat(new SortQuery("").getString(), is(StringUtils.EMPTY));
    assertThat(new SortQuery("", Sort.DESC).getString(), is(StringUtils.EMPTY));
    assertThat(new SortQuery("foo").getString(), is(String.format("%s=foo asc", SortQuery.PARAMETER)));
    assertThat(new SortQuery("foo", Sort.ASC).getString(), is(String.format("%s=foo asc", SortQuery.PARAMETER)));
    assertThat(new SortQuery("foo", Sort.DESC).getString(), is(String.format("%s=foo desc", SortQuery.PARAMETER)));
  }
}
