package com.valtech.aemsaas.core.services.impl;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aemsaas.core.models.request.SearchRequest;
import com.valtech.aemsaas.core.models.response.search.SearchResponse;
import com.valtech.aemsaas.core.models.search.payload.DefaultFulltextSearchGetRequestPayload;
import com.valtech.aemsaas.core.models.search.query.DefaultLanguageQuery;
import com.valtech.aemsaas.core.models.search.query.DefaultTermQuery;
import com.valtech.aemsaas.core.models.search.payload.FulltextSearchGetRequestPayload;
import com.valtech.aemsaas.core.services.search.SearchRequestExecutorService;
import com.valtech.aemsaas.core.services.search.impl.DefaultFulltextSearchService;
import com.valtech.aemsaas.core.services.search.impl.DefaultSearchServiceConnectionConfigurationService;
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
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchGetRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    assertThrows(IllegalArgumentException.class, () -> testee.getResults(null, payload));
    assertThrows(IllegalArgumentException.class, () -> testee.getResults("", payload));
  }

  @Test
  void testGetResults_failedRequestExecution() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(Optional.empty());
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchGetRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    MatcherAssert.assertThat(testee.getResults("foo", payload).isPresent(), is(false));
  }

  @Test
  void testGetResults_responseBodyMissing() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject())));
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchGetRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    MatcherAssert.assertThat(testee.getResults("foo", payload).isPresent(), is(false));
  }

  @Test
  void testGetResults_ok() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
            .getAsJsonObject())));
    FulltextSearchGetRequestPayload payload = DefaultFulltextSearchGetRequestPayload.builder(
        new DefaultTermQuery("bar"), new DefaultLanguageQuery("de")).build();
    MatcherAssert.assertThat(testee.getResults("foo", payload).isPresent(), is(true));
  }
}
