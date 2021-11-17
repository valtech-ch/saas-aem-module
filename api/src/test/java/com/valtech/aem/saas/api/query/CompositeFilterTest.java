package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import org.junit.jupiter.api.Test;

class CompositeFilterTest {

  @Test
  void testGetString() {
    assertThat(CompositeFilter.builder()
        .build().getQueryString(), isEmptyString());
    assertThat(CompositeFilter.builder()
        .filter(new SimpleFilter("foo", "bar"))
        .filter(new SimpleFilter("baz", "quz"))
        .build().getQueryString(), is("(foo:bar OR baz:quz)"));
    assertThat(CompositeFilter.builder()
        .filter(new SimpleFilter("foo", "bar"))
        .filter(new SimpleFilter("baz", "quz"))
        .joinOperator(FilterJoinOperator.AND)
        .build().getQueryString(), is("(foo:bar AND baz:quz)"));
    assertThat(CompositeFilter.builder()
        .filter(CompositeFilter.builder().joinOperator(FilterJoinOperator.OR).filter(new SimpleFilter("it", "is"))
            .filter(new SimpleFilter("operator", "example")).filter(new SimpleFilter("it", "is not")).build())
        .filter(new SimpleFilter("baz", "quz"))
        .joinOperator(FilterJoinOperator.AND)
        .build().getQueryString(), is("((it:is OR operator:example OR it:is not) AND baz:quz)"));
  }
}
