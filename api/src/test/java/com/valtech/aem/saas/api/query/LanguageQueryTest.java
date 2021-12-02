package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LanguageQueryTest {

    @Test
    void testQuery() {
        assertThrows(IllegalArgumentException.class, () -> new LanguageQuery(null));
        assertThrows(IllegalArgumentException.class, () -> new LanguageQuery(""));
        assertThat(new LanguageQuery("de").getEntries().size(), is(1));
    }
}
