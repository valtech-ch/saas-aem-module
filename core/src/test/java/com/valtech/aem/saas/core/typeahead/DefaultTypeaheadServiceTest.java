package com.valtech.aem.saas.core.typeahead;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.typeahead.TypeaheadPayload;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStreamReader;
import java.util.Optional;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultTypeaheadServiceTest {

  public static final String SAMPLE_ALLOWED_FIELD = "language";

  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  TypeaheadService service;

  @BeforeEach
  void setUp(AemContext context) {
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    context.registerService(SearchRequestExecutorService.class, searchRequestExecutorService);
    service = context.registerInjectActivateService(new DefaultTypeaheadService());
  }

  @Test
  void testInputValidation() {
    TypeaheadPayload emptyPayload = getPayloadInstance();
    testImproperPayload(service, emptyPayload);

    TypeaheadPayload noTermPayload = DefaultTypeaheadPayload.builder().language("de").build();
    testImproperPayload(service, noTermPayload);

    TypeaheadPayload noLanguagePayload = DefaultTypeaheadPayload.builder().text("foo bar").build();
    testImproperPayload(service, noLanguagePayload);

    TypeaheadPayload forbiddenFilterFieldsPayload = DefaultTypeaheadPayload.builder().text("foo bar")
        .filterEntry("forbiddenKey", "val").build();
    testImproperPayload(service, forbiddenFilterFieldsPayload);

    TypeaheadPayload properPayload = getProperPayload();
    assertDoesNotThrow(() -> service.getResults("indexfoo", properPayload));
  }

  @Test
  void testGetResults() {
    when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/typeahead/success.json")))
            .getAsJsonObject(), true)));
    assertThat(service.getResults("indexfoo", getProperPayload()), is(not(empty())));
  }

  @Test
  void testGetResults_noTypeaheadOptions() {
    when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/typeahead/empty.json")))
            .getAsJsonObject(), true)));
    assertThat(service.getResults("indexfoo", getProperPayload()), is(empty()));
  }

  private void testImproperPayload(TypeaheadService typeaheadService,
      TypeaheadPayload payload) {
    assertThrows(IllegalArgumentException.class, () -> typeaheadService.getResults("indexfoo", payload));
  }

  private TypeaheadPayload getProperPayload() {
    return DefaultTypeaheadPayload.builder()
        .text("foo bar")
        .language("de")
        .filterEntry(SAMPLE_ALLOWED_FIELD, "val")
        .build();
  }

  private TypeaheadPayload getPayloadInstance() {
    return DefaultTypeaheadPayload.builder().build();
  }
}
