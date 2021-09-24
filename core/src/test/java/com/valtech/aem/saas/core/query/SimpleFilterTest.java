package com.valtech.aem.saas.core.query;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class SimpleFilterTest {

  @Test
  void testGetString() {
    MatcherAssert.assertThat(new SimpleFilter(null, null).getString(), isEmptyString());
    MatcherAssert.assertThat(new SimpleFilter("", "").getString(), isEmptyString());
    MatcherAssert.assertThat(new SimpleFilter("foo", "").getString(), isEmptyString());
    MatcherAssert.assertThat(new SimpleFilter("foo", null).getString(), isEmptyString());
    MatcherAssert.assertThat(new SimpleFilter("", "foo").getString(), isEmptyString());
    MatcherAssert.assertThat(new SimpleFilter(null, "foo").getString(), isEmptyString());
    MatcherAssert.assertThat(new SimpleFilter("foo", "bar").getString(), is("foo:bar"));
  }
}
