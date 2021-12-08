package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

class FiltersQueryTest {

    @Test
    void testQuery() {
        assertThat(FiltersQuery.builder().build().getEntries(), is(empty()));
        assertThat(FiltersQuery.builder()
                               .filter(new SimpleFilter("foo", ""))
                               .filter(new SimpleFilter("bar", null)).build()
                               .getEntries(), is(empty()));
        assertThat(FiltersQuery.builder()
                               .filter(new SimpleFilter("foo", "FOO"))
                               .filter(new SimpleFilter("bar", "BAR")).build()
                               .getEntries().size(), is(2));
        assertThat(FiltersQuery.builder()
                               .filter(new SimpleFilter("foo", ""))
                               .filter(new SimpleFilter("bar", null))
                               .filter(new SimpleFilter("baz", "BAZ")).build()
                               .getEntries().size(), is(1));
    }
}
