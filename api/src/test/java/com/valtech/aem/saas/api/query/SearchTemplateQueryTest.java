package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchTemplateQueryTest {

    @Test
    void testQuery() {
        assertThrows(IllegalArgumentException.class, () -> new SearchTemplateQuery(null));
        assertThat(new SearchTemplateQuery("foo").getEntries().size(), is(1));
    }
}
