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
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import java.io.InputStreamReader;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IndexTypeaheadConsumerServiceTest {

  public static final String SAMPLE_ALLOWED_FIELD = "allowedField";
  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  @Test
  void testCommonConfigValidation() {
    IndexTypeaheadConsumerService emptyIndexTypeaheadConsumerService = IndexTypeaheadConsumerService.builder().build();
    TypeaheadPayload payload = getPayloadInstance();
    assertThrows(IllegalStateException.class, () -> emptyIndexTypeaheadConsumerService.getResults(payload));

    IndexTypeaheadConsumerService noApiUrlIndexTypeaheadConsumerService = IndexTypeaheadConsumerService.builder()
        .searchRequestExecutorService(searchRequestExecutorService).build();
    assertThrows(IllegalStateException.class, () -> noApiUrlIndexTypeaheadConsumerService.getResults(payload));

    IndexTypeaheadConsumerService noSearchRequestExecutorServiceIndexTypeaheadConsumerService = IndexTypeaheadConsumerService.builder()
        .apiUrl("foo").build();
    assertThrows(IllegalStateException.class,
        () -> noSearchRequestExecutorServiceIndexTypeaheadConsumerService.getResults(payload));
  }

  @Test
  void testInputValidation() {
    IndexTypeaheadConsumerService indexTypeaheadConsumerService = IndexTypeaheadConsumerService.builder()
        .apiUrl("foo")
        .searchRequestExecutorService(searchRequestExecutorService)
        .allowedFilterField(SAMPLE_ALLOWED_FIELD)
        .build();

    TypeaheadPayload emptyPayload = getPayloadInstance();
    testImproperPayload(indexTypeaheadConsumerService, emptyPayload);

    TypeaheadPayload noTermPayload = DefaultTypeaheadPayload.builder().language("de").build();
    testImproperPayload(indexTypeaheadConsumerService, noTermPayload);

    TypeaheadPayload noLanguagePayload = DefaultTypeaheadPayload.builder().text("foo bar").build();
    testImproperPayload(indexTypeaheadConsumerService, noLanguagePayload);

    TypeaheadPayload forbiddenFilterFieldsPayload = DefaultTypeaheadPayload.builder().text("foo bar")
        .filterEntry("forbiddenKey", "val").build();
    testImproperPayload(indexTypeaheadConsumerService, forbiddenFilterFieldsPayload);

    TypeaheadPayload properPayload = getProperPayload();
    assertDoesNotThrow(() -> indexTypeaheadConsumerService.getResults(properPayload));
  }

  @Test
  void testGetResults() {
    when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/typeahead/success.json")))
            .getAsJsonObject(), true)));
    IndexTypeaheadConsumerService indexTypeaheadConsumerService = IndexTypeaheadConsumerService.builder()
        .apiUrl("foo")
        .searchRequestExecutorService(searchRequestExecutorService)
        .build();
    assertThat(indexTypeaheadConsumerService.getResults(getProperPayload()), is(not(empty())));
  }

  @Test
  void testGetResults_noTypeaheadOptions() {
    when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/typeahead/empty.json")))
            .getAsJsonObject(), true)));
    IndexTypeaheadConsumerService indexTypeaheadConsumerService = IndexTypeaheadConsumerService.builder()
        .apiUrl("foo")
        .searchRequestExecutorService(searchRequestExecutorService)
        .build();
    assertThat(indexTypeaheadConsumerService.getResults(getProperPayload()), is(empty()));
  }

  private void testImproperPayload(IndexTypeaheadConsumerService indexTypeaheadConsumerService,
      TypeaheadPayload payload) {
    assertThrows(IllegalArgumentException.class, () -> indexTypeaheadConsumerService.getResults(payload));
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
