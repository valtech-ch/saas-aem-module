package com.valtech.aem.saas.core.http.request;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

class SearchRequestDeleteTest {

  @Test
  void testBuilder() {
    SearchRequestDelete.SearchRequestDeleteBuilder builder = SearchRequestDelete.builder();
    assertThrows(NullPointerException.class, builder::build);
  }

  @Test
  void getRequest() {
    SearchRequestDelete emptySearchRequestDelete = SearchRequestDelete.builder().uri("").build();
    assertThrows(IllegalArgumentException.class, emptySearchRequestDelete::getRequest);
    SearchRequestDelete incorrectUriSyntaxSearchRequestDelete = SearchRequestDelete.builder().uri("$%^&*").build();
    assertThrows(IllegalArgumentException.class, incorrectUriSyntaxSearchRequestDelete::getRequest);
    assertThat(SearchRequestDelete.builder().uri("https://wknd.site/us/en/adventures/bali-surf-camp.html").build()
        .getRequest(), IsInstanceOf.instanceOf(
        HttpUriRequest.class));
  }

  @Test
  void getSuccessStatusCodes() {
    List<Integer> statusCodes = SearchRequestDelete.builder().uri("").build().getSuccessStatusCodes();
    assertThat(statusCodes.size(), is(1));
    assertThat(statusCodes.get(0), is(
        HttpServletResponse.SC_OK));
  }
}
