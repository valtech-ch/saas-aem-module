package com.valtech.aem.saas.core.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import org.junit.jupiter.api.Test;

class CompositeFilterTest {

  @Test
  void testGetString() {
    assertThat(CompositeFilter.builder()
        .build().getString(), isEmptyString());
    assertThat(CompositeFilter.builder()
        .filter(new SimpleFilter("foo", "bar"))
        .filter(new SimpleFilter("baz", "quz"))
        .build().getString(), is("(foo:bar baz:quz)"));
    assertThat(CompositeFilter.builder()
        .filter(new SimpleFilter("foo", "bar"))
        .filter(new SimpleFilter("baz", "quz"))
        .joinOperator(FilterJoinOperator.OR)
        .build().getString(), is("(foo:bar OR baz:quz)"));
    assertThat(CompositeFilter.builder()
        .filter(CompositeFilter.builder().joinOperator(FilterJoinOperator.OR).filter("it", "is")
            .filter("operator", "example").filter("it", "is not").build())
        .filter(new SimpleFilter("baz", "quz"))
        .build().getString(), is("((it:is OR operator:example OR it:is not) baz:quz)"));
  }
}
