package com.valtech.aem.saas.api.query;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FacetsQueryTest {

    @Test
    void testQuery() {
        assertThat(FacetsQuery.builder().build().getEntries(), is(Matchers.empty()));
        assertThat(FacetsQuery.builder().field(null).build().getEntries(), is(Matchers.empty()));
        assertThat(FacetsQuery.builder().field("").build().getEntries(), is(Matchers.empty()));
        assertThat(FacetsQuery.builder().field(null).field("foo").build().getEntries().size(),
                   is(1));
        assertThat(FacetsQuery.builder().field("").field("foo").build().getEntries().size(),
                   is(1));
        assertThat(FacetsQuery.builder().field("foo").field("bar").build().getEntries().size(),
                   is(2));
    }
}
