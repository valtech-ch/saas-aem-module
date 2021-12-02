package com.valtech.aem.saas.core.http.client;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class HttpHostResolverTest {

    @Test
    void getHost() {
        assertThat(new HttpHostResolver("www.google.com").getHost().isPresent(), is(false));
        assertThat(new HttpHostResolver("http://www.google.com").getHost().isPresent(), is(true));
    }
}
