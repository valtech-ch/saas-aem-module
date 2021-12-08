package com.valtech.aem.saas.core.fulltextsearch;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

class FacetFilterParserTest {

    @Test
    void testParser() {
        FacetFilterParser facetFilterParser = new FacetFilterParser("contentType:zip,xml,pdf");
        assertThat(facetFilterParser.getKey(), is("contentType"));
        assertThat(facetFilterParser.getValues(), not(empty()));
        assertThat(facetFilterParser.getValues().size(), is(3));

        FacetFilterParser emptyTextParser = new FacetFilterParser("");
        assertThat(emptyTextParser.getKey(), isEmptyString());
        assertThat(emptyTextParser.getValues(), empty());

        Assertions.assertThrows(NullPointerException.class, () -> new FacetFilterParser(null));
    }
}
