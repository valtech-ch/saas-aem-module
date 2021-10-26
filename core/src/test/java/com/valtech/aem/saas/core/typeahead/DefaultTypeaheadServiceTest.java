package com.valtech.aem.saas.core.typeahead;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.typeahead.dto.DefaultTypeaheadPayloadDTO;
import com.valtech.aem.saas.api.typeahead.dto.TypeaheadPayloadDTO;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.fulltextsearch.FilterModelImpl;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.api.request.SearchRequest;
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
    TypeaheadPayloadDTO emptyPayload = getPayloadInstance();
    testImproperPayload(service, emptyPayload);

    TypeaheadPayloadDTO noTermPayload = DefaultTypeaheadPayloadDTO.builder().language("de").build();
    testImproperPayload(service, noTermPayload);

    TypeaheadPayloadDTO noLanguagePayload = DefaultTypeaheadPayloadDTO.builder().text("foo bar").build();
    testImproperPayload(service, noLanguagePayload);

    TypeaheadPayloadDTO forbiddenFilterFieldsPayload = DefaultTypeaheadPayloadDTO.builder().text("foo bar")
        .filter(new FilterModelImpl("forbiddenKey", "val")).build();
    testImproperPayload(service, forbiddenFilterFieldsPayload);

    TypeaheadPayloadDTO properPayload = getProperPayload();
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
      TypeaheadPayloadDTO payload) {
    assertThrows(IllegalArgumentException.class, () -> typeaheadService.getResults("indexfoo", payload));
  }

  private TypeaheadPayloadDTO getProperPayload() {
    return DefaultTypeaheadPayloadDTO.builder()
        .text("foo bar")
        .language("de")
        .filter(new FilterModelImpl(SAMPLE_ALLOWED_FIELD, "val"))
        .build();
  }

  private TypeaheadPayloadDTO getPayloadInstance() {
    return DefaultTypeaheadPayloadDTO.builder().build();
  }
}
