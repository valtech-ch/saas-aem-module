package com.valtech.aem.saas.core.http.request;

import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchRequestGetTest {

    @Test
    void testConstructor() {
        assertThrows(NullPointerException.class, () -> new SearchRequestGet(null));
    }

    @Test
    void getRequest() {
        SearchRequestGet emptySearchRequestGet = new SearchRequestGet("");
        assertThrows(IllegalArgumentException.class, emptySearchRequestGet::getRequest);
        SearchRequestGet incorrectUriSyntaxSearchRequestGet = new SearchRequestGet("$%^&*");
        assertThrows(IllegalArgumentException.class, incorrectUriSyntaxSearchRequestGet::getRequest);
        assertThat(new SearchRequestGet("https://wknd.site/us/en/adventures/bali-surf-camp.html").getRequest(),
                   IsInstanceOf.instanceOf(
                           HttpUriRequest.class));
    }

    @Test
    void getSuccessStatusCodes() {
        List<Integer> statusCodes = new SearchRequestGet("").getSuccessStatusCodes();
        assertThat(statusCodes.size(), is(1));
        assertThat(statusCodes.get(0), is(
                HttpServletResponse.SC_OK));
    }
}
