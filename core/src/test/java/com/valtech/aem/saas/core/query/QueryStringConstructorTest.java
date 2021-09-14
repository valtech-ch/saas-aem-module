package com.valtech.aem.saas.core.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class QueryStringConstructorTest {

  @Test
  void testGetQueryString() {
    assertThat(GetQueryStringConstructor.builder()
            .query(new DefaultTermQuery("foo"))
            .build().getQueryString(),
        is("?term=foo"));

    assertThat(GetQueryStringConstructor.builder()
            .query(new DefaultTermQuery("foo"))
            .queries(Arrays.asList(new PaginationQuery(1, 100, 1000),
                FiltersQuery.builder().filterEntry("bar", "/foo/baz").build()))
            .build().getQueryString(),
        is("?term=foo&start=1&rows=100&filter=bar%3A%2Ffoo%2Fbaz"));
  }
}
