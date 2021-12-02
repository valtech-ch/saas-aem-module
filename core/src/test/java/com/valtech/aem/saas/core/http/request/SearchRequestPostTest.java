package com.valtech.aem.saas.core.http.request;

import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SearchRequestPostTest {

    @Test
    void testBuilder() {
        SearchRequestPost.SearchRequestPostBuilder builder = SearchRequestPost.builder();
        assertThrows(NullPointerException.class, builder::build);
    }

    @Test
    void getRequest() {
        SearchRequestPost emptySearchRequestPost = SearchRequestPost.builder().uri("").build();
        assertThrows(IllegalArgumentException.class, emptySearchRequestPost::getRequest);
        SearchRequestPost incorrectUriSyntaxSearchRequestPost = SearchRequestPost.builder().uri("$%^&*").build();
        assertThrows(IllegalArgumentException.class, incorrectUriSyntaxSearchRequestPost::getRequest);
        assertThat(
                SearchRequestPost.builder()
                                 .uri("https://wknd.site/us/en/adventures/bali-surf-camp.html")
                                 .build()
                                 .getRequest(),
                IsInstanceOf.instanceOf(
                        HttpUriRequest.class));
    }

    @Test
    void getSuccessStatusCodes() {
        List<Integer> statusCodes = SearchRequestPost.builder().uri("").build().getSuccessStatusCodes();
        assertThat(statusCodes.size(), is(2));
        assertThat(statusCodes.get(0), is(
                HttpServletResponse.SC_OK));
        assertThat(statusCodes.get(1), is(
                HttpServletResponse.SC_CREATED));
    }

}
