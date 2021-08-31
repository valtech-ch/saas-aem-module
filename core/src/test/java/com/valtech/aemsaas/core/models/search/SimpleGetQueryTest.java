package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class SimpleGetQueryTest {

  @Test
  void getString() {
    assertThat(new SimpleGetQuery("foo", "").getString(), is(StringUtils.EMPTY));
    assertThat(new SimpleGetQuery("foo", null).getString(), is(StringUtils.EMPTY));
    assertThat(new SimpleGetQuery("", "bar").getString(), is(StringUtils.EMPTY));
    assertThat(new SimpleGetQuery(null, "bar").getString(), is(StringUtils.EMPTY));
    assertThat(new SimpleGetQuery("foo", "bar").getString(), is("foo=bar"));
  }
}
