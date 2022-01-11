package com.valtech.aem.saas.core.http.request;

import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

class SearchRequestHeadTest {

    @Test
    void testConstructor() {
        assertThrows(NullPointerException.class, () -> new SearchRequestHead(null));
    }

    @Test
    void getRequest() {
        SearchRequestHead emptySearchRequestHead = new SearchRequestHead("");
        assertThrows(IllegalArgumentException.class, emptySearchRequestHead::getRequest);
        SearchRequestHead incorrectUriSyntaxSearchRequestHead = new SearchRequestHead("$%^&*");
        assertThrows(IllegalArgumentException.class, incorrectUriSyntaxSearchRequestHead::getRequest);
        assertThat(new SearchRequestHead("https://wknd.site/us/en/adventures/bali-surf-camp.html").getRequest(),
                IsInstanceOf.instanceOf(
                        HttpUriRequest.class));
    }

    @Test
    void getSuccessStatusCodes() {
        List<Integer> statusCodes = new SearchRequestHead("").getSuccessStatusCodes();
        assertThat(statusCodes.size(), is(1));
        assertThat(statusCodes.get(0), is(
                HttpServletResponse.SC_OK));
    }
}