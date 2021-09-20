package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConsumerService;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.DefaultTermQuery;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStreamReader;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class IndexFulltextSearchConsumerServiceTest {

  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  FulltextSearchConsumerService testee;

  @BeforeEach
  void setUp(AemContext context) {
    context.registerService(SearchRequestExecutorService.class, searchRequestExecutorService);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    testee = context.registerInjectActivateService(new DefaultFulltextSearchService())
        .getFulltextSearchConsumerService("foo", null);
  }

  @Test
  void testGetResults_failedRequestExecution() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(Optional.empty());
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    assertThat(testee.getResults(payload).isPresent(), is(false));
  }

  @Test
  void testGetResults_responseBodyMissing() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    assertThat(testee.getResults(payload).isPresent(), is(false));
  }

  @Test
  void testGetResults_ok() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
            .getAsJsonObject(), true)));
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    assertThat(testee.getResults(payload).isPresent(), is(true));
  }
}
