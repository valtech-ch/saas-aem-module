package com.valtech.aem.saas.api.query;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

class SimpleFilterTest {

    @Test
    void testGetQueryString() {
        assertThat(new SimpleFilter(null, null, false).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("", "", false).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", "", false).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", null, false).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("", "foo", false).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter(null, "foo", false).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", "bar", false).getQueryString(), is("foo:bar"));
        assertThat(new SimpleFilter("foo", "bar quz", false).getQueryString(), is("foo:\"bar quz\""));
    }

    @Test
    void testStartsWithQueryString() {
        assertThat(new SimpleFilter(null, null, true).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("", "", true).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", "", true).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", null, true).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("", "foo", true).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter(null, "foo", true).getQueryString(), isEmptyString());
        assertThat(new SimpleFilter("foo", "bar", true).getQueryString(), is("foo:bar"));
        assertThat(new SimpleFilter("foo", "bar*", true).getQueryString(), is("foo:bar*"));
        assertThat(new SimpleFilter("foo", "bar quz", true).getQueryString(), is("foo:\"bar quz\""));
        assertThat(new SimpleFilter("foo", "bar/quz*", true).getQueryString(), is("foo:bar\\/quz*"));
    }
}
