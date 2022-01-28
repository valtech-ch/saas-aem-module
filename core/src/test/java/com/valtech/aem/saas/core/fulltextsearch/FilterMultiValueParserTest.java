package com.valtech.aem.saas.core.fulltextsearch;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;

class FilterMultiValueParserTest {

    @Test
    void testGetValues() {
        assertThat(new FilterMultiValueParser(null).getValues(), empty());
        assertThat(new FilterMultiValueParser("").getValues(), empty());
        assertThat(new FilterMultiValueParser("foo").getValues().size(), Is.is(1));
        assertThat(new FilterMultiValueParser("foo,bar").getValues().size(), Is.is(2));
    }
}
