package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class QueryStringConstructorTest {

    @Test
    void testGetQueryString() {
        assertThat(GetQueryStringConstructor.builder()
                                            .query(new TermQuery("foo"))
                                            .build().getQueryString(),
                   is("?term=foo"));

        assertThat(GetQueryStringConstructor.builder()
                                            .query(new TermQuery("foo"))
                                            .queries(Arrays.asList(new PaginationQuery(1, 100),
                                                                   FiltersQuery.builder()
                                                                               .filter(new SimpleFilter("bar",
                                                                                                        "/foo/baz"))
                                                                               .build()))
                                            .build().getQueryString(),
                   is("?term=foo&start=1&rows=100&filter=bar%3A%2Ffoo%2Fbaz"));
    }
}
