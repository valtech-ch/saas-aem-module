package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class HighlightingDisableQueryTest {

    @Test
    void testQuery() {
        assertThat(new HighlightingDisableQuery().getEntries().size(),
                   is(1));
    }
}
