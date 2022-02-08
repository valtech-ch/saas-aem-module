package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

class SimpleFilterTest {

    @Test
    void testGetQueryString() {
        assertThat(new SimpleFilter(null, null).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("", "").getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", "").getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", null).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("", "foo").getQueryString(), isEmptyString());
        assertThat(new SimpleFilter(null, "foo").getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", "bar").getQueryString(), is("foo:bar"));
        assertThat(new SimpleFilter("foo", "bar quz").getQueryString(), is("foo:\"bar quz\""));
    }
}
