package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import org.junit.jupiter.api.Test;

class NotFilterTest {

    @Test
    void testGetQueryString() {
        assertThat(new NotFilter(null, null, false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("", "", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", "", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", null, false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("", "foo", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter(null, "foo", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", "bar", false).getQueryString(), is("NOT foo:bar"));
        assertThat(new NotFilter("foo", "bar quz", false).getQueryString(), is("NOT foo:\"bar quz\""));
    }

    @Test
    void testStartsWithQueryString() {
        assertThat(new NotFilter(null, null, false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("", "", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", "", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", null, false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("", "foo", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter(null, "foo", false).getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", "bar", false).getQueryString(), is("NOT foo:bar"));
        assertThat(new NotFilter("foo", "bar quz", false).getQueryString(), is("NOT foo:\"bar quz\""));
    }

}
