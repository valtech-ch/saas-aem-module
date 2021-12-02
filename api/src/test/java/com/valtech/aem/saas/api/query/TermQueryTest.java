package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class TermQueryTest {

    @Test
    void testQuery() {
        assertThat(new TermQuery(null).getEntries().size(), is(1));
        assertThat(new TermQuery("").getEntries().size(), is(1));
        assertThat(new TermQuery("foo").getEntries().size(), is(1));
    }
}
