package com.valtech.aem.saas.core.http.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

class SearchRequestPutTest {

  @Test
  void testBuilder() {
    SearchRequestPut.SearchRequestPutBuilder builder = SearchRequestPut.builder();
    assertThrows(NullPointerException.class, builder::build);
  }

  @Test
  void getRequest() {
    SearchRequestPut emptySearchRequestPut = SearchRequestPut.builder().uri("").build();
    assertThrows(IllegalArgumentException.class, emptySearchRequestPut::getRequest);
    SearchRequestPut incorrectUriSyntaxSearchRequestPut = SearchRequestPut.builder().uri("$%^&*").build();
    assertThrows(IllegalArgumentException.class, incorrectUriSyntaxSearchRequestPut::getRequest);
    assertThat(
        SearchRequestPut.builder().uri("https://wknd.site/us/en/adventures/bali-surf-camp.html").build().getRequest(),
        IsInstanceOf.instanceOf(
            HttpUriRequest.class));
  }

  @Test
  void getSuccessStatusCodes() {
    List<Integer> statusCodes = SearchRequestPut.builder().uri("").build().getSuccessStatusCodes();
    assertThat(statusCodes.size(), is(3));
    assertThat(statusCodes.get(0), is(
        HttpServletResponse.SC_OK));
    assertThat(statusCodes.get(1), is(
        HttpServletResponse.SC_CREATED));
    assertThat(statusCodes.get(2), is(
        HttpServletResponse.SC_NO_CONTENT));
  }

}
