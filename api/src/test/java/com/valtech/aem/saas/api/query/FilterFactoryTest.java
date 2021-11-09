package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.util.Arrays;
import java.util.Collections;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

class FilterFactoryTest {


  @Test
  void testCreateFilter() {
    Filter filter1 = FilterFactory.createFilter("foo", "bar");
    assertThat(filter1, instanceOf(SimpleFilter.class));
    assertThat(filter1.getQueryString(), Is.is("foo:bar"));

    Filter filter2 = FilterFactory.createFilter("foo", Arrays.asList("bar", "baz"));
    assertThat(filter2, instanceOf(CompositeFilter.class));
    assertThat(filter2.getQueryString(), Is.is("(foo:bar OR foo:baz)"));

    Filter filter3 = FilterFactory.createFilter("foo", Collections.emptyList());
    assertThat(filter3, instanceOf(SimpleFilter.class));
    assertThat(filter3.getQueryString(), Is.is(StringUtils.EMPTY));
  }
}
