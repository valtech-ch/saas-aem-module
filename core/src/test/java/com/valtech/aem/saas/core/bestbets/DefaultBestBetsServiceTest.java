package com.valtech.aem.saas.core.bestbets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.bestbets.dto.BestBetPayloadDTO;
import com.valtech.aem.saas.api.bestbets.BestBetsActionFailedException;
import com.valtech.aem.saas.api.bestbets.dto.DefaultBestBetPayloadDTO;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultBestBetsServiceTest {

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  DefaultBestBetsService testee;

  @BeforeEach
  void setUp(AemContext context) {
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    context.registerService(SearchRequestExecutorService.class, searchRequestExecutorService);
    testee = context.registerInjectActivateService(new DefaultBestBetsService());
  }

  @Test
  void testAddBestBet() {
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    assertDoesNotThrow(() -> testee.addBestBet("clientfoo",
        new DefaultBestBetPayloadDTO("foo", "baz", "bar", "de")));
  }

  @Test
  void testAddBestBet_failed() {
    DefaultBestBetPayloadDTO payload = new DefaultBestBetPayloadDTO("foo", "baz", "bar", "de");
    assertThrows(BestBetsActionFailedException.class, () -> testee.addBestBet("clientfoo", payload));
  }

  @Test
  void testAddBestBets() {
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    assertDoesNotThrow(() -> testee.addBestBets("clientfoo", Collections.singletonList(
        new DefaultBestBetPayloadDTO("foo", "baz", "bar", "de"))));
  }

  @Test
  void testAddBestBets_failed() {
    List<BestBetPayloadDTO> payload = Collections.singletonList(
        new DefaultBestBetPayloadDTO("foo", "baz", "bar", "de"));
    assertThrows(BestBetsActionFailedException.class, () -> testee.addBestBets("clientfoo", payload));
  }

  @Test
  void testUpdateBestBet() {
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(
                    getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
            .getAsJsonObject(), true)));
    assertDoesNotThrow(() -> testee.updateBestBet("clientfoo", 1,
        new DefaultBestBetPayloadDTO("foo", "baz", "bar", "de")));
  }

  @Test
  void testUpdateBestBet_missingAction(AemContext context) {
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiUpdateBestBetAction", "")
            .build());
    DefaultBestBetPayloadDTO payload = new DefaultBestBetPayloadDTO("foo", "baz", "bar", "de");
    assertThrows(IllegalStateException.class, () -> testee.updateBestBet("clientfoo", 1,
        payload));
  }

  @Test
  void testUpdateBestBet_failed() {
    BestBetPayloadDTO payload = new DefaultBestBetPayloadDTO("foo", "baz", "bar", "de");
    assertThrows(BestBetsActionFailedException.class, () -> testee.updateBestBet("clientfoo", 1, payload));
  }

  @Test
  void testDeleteBestBet() {
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(
                    getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
            .getAsJsonObject(), true)));
    assertDoesNotThrow(() -> testee.deleteBestBet("clientfoo", 1));
  }

  @Test
  void testDeleteBestBet_missingAction(AemContext context) {
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiDeleteBestBetAction", "")
            .build());
    assertThrows(IllegalStateException.class, () -> testee.deleteBestBet("clientfoo", 1));
  }

  @Test
  void testDeleteBestBet_failed() {
    assertThrows(BestBetsActionFailedException.class, () -> testee.deleteBestBet("clientfoo", 1));
  }

  @Test
  void testPublishBestBetsForProject() {
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    assertDoesNotThrow(() -> testee.publishBestBetsForProject("clientfoo", 1));
  }

  @Test
  void testPublishBestBetsForProject_wrongConfigForAction(AemContext context) {
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiPublishProjectBestBetsAction", "/bestbets")
            .build());
    assertThrows(IllegalArgumentException.class, () -> testee.publishBestBetsForProject("clientfoo", 1));
  }

  @Test
  void testPublishBestBetsForProject_failed() {
    assertThrows(BestBetsActionFailedException.class, () -> testee.publishBestBetsForProject("clientfoo", 1));
  }

  @Test
  void testGetBestBets() {
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/getBestBets.json")))
            .getAsJsonArray(), true)));
    assertThat(testee.getBestBets("clientfoo"), not(empty()));
  }

  @Test
  void testGetBestBets_missingAction(AemContext context) {
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiGetAllBestBetsAction", "")
            .build());
    assertThrows(IllegalStateException.class, () -> testee.getBestBets("clientfoo"));
  }

  @Test
  void testGetBestBets_failed() {
    assertThrows(BestBetsActionFailedException.class, () -> testee.getBestBets("clientfoo"));
  }

}
