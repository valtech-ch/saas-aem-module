package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.fulltextsearch.dto.DefaultFulltextSearchRequestPayloadDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchPayloadDTO;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.TermQuery;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStreamReader;
import java.util.Optional;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultFulltextSearchServiceTest {

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  FulltextSearchService service;
  FulltextSearchConfigurationService configService;

  @BeforeEach
  void setUp(AemContext context) {
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    context.registerService(SearchRequestExecutorService.class, searchRequestExecutorService);
    service = context.registerInjectActivateService(new DefaultFulltextSearchService());
    configService = context.registerInjectActivateService(new DefaultFulltextSearchService());
  }

  @Test
  void testNullArguments() {
    FulltextSearchPayloadDTO payload = DefaultFulltextSearchRequestPayloadDTO.builder(
        new TermQuery("bar"), new LanguageQuery("de")).build();
    Assertions.assertThrows(NullPointerException.class, () -> service.getResults(null, payload, false, false));
    Assertions.assertThrows(NullPointerException.class, () -> service.getResults("indexfoo", null, false, false));
  }

  @Test
  void testBlankIndexArgument() {
    FulltextSearchPayloadDTO payload = DefaultFulltextSearchRequestPayloadDTO.builder(
        new TermQuery("bar"), new LanguageQuery("de")).build();
    Assertions.assertThrows(IllegalArgumentException.class, () -> service.getResults("", payload, false, false));
  }

  @Test
  void testGetResults_failedRequestExecution() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(Optional.empty());
    FulltextSearchPayloadDTO payload = DefaultFulltextSearchRequestPayloadDTO.builder(
        new TermQuery("bar"), new LanguageQuery("de")).build();
    assertThat(service.getResults("indexfoo", payload, false, false).isPresent(), is(false));
  }

  @Test
  void testGetResults_responseBodyMissing() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    FulltextSearchPayloadDTO payload = DefaultFulltextSearchRequestPayloadDTO.builder(
        new TermQuery("bar"), new LanguageQuery("de")).build();
    assertThat(service.getResults("indexfoo", payload, false, false).isPresent(), is(false));
  }

  @Test
  void testGetResults_ok() {
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
            .getAsJsonObject(), true)));
    FulltextSearchPayloadDTO payload = DefaultFulltextSearchRequestPayloadDTO.builder(
        new TermQuery("bar"), new LanguageQuery("de")).build();
    assertThat(service.getResults("indexfoo", payload, false, false).isPresent(), is(true));
  }

  @Test
  void testGetRowsMaxLimit() {
    assertThat(configService.getRowsMaxLimit(), is(9999));
  }

}
