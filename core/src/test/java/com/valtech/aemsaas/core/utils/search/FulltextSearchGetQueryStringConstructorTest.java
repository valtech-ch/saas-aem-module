package com.valtech.aemsaas.core.utils.search;

import static org.hamcrest.core.Is.is;

import com.valtech.aemsaas.core.models.search.FiltersQuery;
import com.valtech.aemsaas.core.models.search.TermQuery;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

class FulltextSearchGetQueryStringConstructorTest {

  @Test
  void testGetQueryString() {
    MatcherAssert.assertThat(FulltextSearchGetQueryStringConstructor.builder()
            .query(new TermQuery("foo"))
            .build().getQueryString(),
        is("?term=foo"));

    MatcherAssert.assertThat(FulltextSearchGetQueryStringConstructor.builder()
            .query(new TermQuery("foo"))
            .query(FiltersQuery.builder().filterEntry("bar", "/foo/baz").build())
            .build().getQueryString(),
        is("?term=foo&filter=bar%3A%2Ffoo%2Fbaz"));
  }
}
