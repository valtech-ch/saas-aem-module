package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.DefaultTermQuery;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStreamReader;
import java.util.Optional;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultFulltextSearchServiceTest {

  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  DefaultFulltextSearchService testee;

  @BeforeEach
  void setUp(AemContext context) {
    context.registerService(SearchRequestExecutorService.class, searchRequestExecutorService);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    testee = context.registerInjectActivateService(new DefaultFulltextSearchService());
  }

  @Test
  void testGetResults_illegalIndexValue() {
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    assertThrows(IllegalArgumentException.class, () -> testee.getResults(null, payload));
    assertThrows(IllegalArgumentException.class, () -> testee.getResults("", payload));
  }

  @Test
  void testGetResults_failedRequestExecution() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(Optional.empty());
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    MatcherAssert.assertThat(testee.getResults("foo", payload).isPresent(), is(false));
  }

  @Test
  void testGetResults_responseBodyMissing() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    MatcherAssert.assertThat(testee.getResults("foo", payload).isPresent(), is(false));
  }

  @Test
  void testGetResults_ok() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
            .getAsJsonObject(), true)));
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    MatcherAssert.assertThat(testee.getResults("foo", payload).isPresent(), is(true));
  }
}
