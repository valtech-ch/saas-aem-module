package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import org.junit.jupiter.api.Test;

public class NotFilterTest {

    @Test
    void testGetQueryString() {
        assertThat(new NotFilter(null, null).getQueryString(), isEmptyString());
        assertThat(new NotFilter("", "").getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", "").getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", null).getQueryString(), isEmptyString());
        assertThat(new NotFilter("", "foo").getQueryString(), isEmptyString());
        assertThat(new NotFilter(null, "foo").getQueryString(), isEmptyString());
        assertThat(new NotFilter("foo", "bar").getQueryString(), is("NOT foo:bar"));
        assertThat(new NotFilter("foo", "bar quz").getQueryString(), is("NOT foo:\"bar quz\""));
    }

}
