package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class TermQueryTest {

  @Test
  void getString() {
    assertThat(new TermQuery(null).getString(), is(StringUtils.EMPTY));
    assertThat(new TermQuery("").getString(), is(StringUtils.EMPTY));
    assertThat(new TermQuery("foo").getString(), is(String.format("%s=foo", TermQuery.PARAMETER)));
  }
}
