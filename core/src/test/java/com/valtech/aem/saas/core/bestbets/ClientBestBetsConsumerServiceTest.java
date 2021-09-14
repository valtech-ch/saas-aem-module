package com.valtech.aem.saas.core.bestbets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.bestbets.BestBetPayload;
import com.valtech.aem.saas.api.bestbets.BestBetsActionFailedException;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientBestBetsConsumerServiceTest {

  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  @BeforeEach
  void setUp() {
  }

  @Test
  void testCommonConfigValidation() {
    testClientBestBetsConsumerServiceIllegalState(ClientBestBetsConsumerService.builder().build());
    testClientBestBetsConsumerServiceIllegalState(ClientBestBetsConsumerService.builder()
        .searchRequestExecutorService(searchRequestExecutorService)
        .build());
    testClientBestBetsConsumerServiceIllegalState(ClientBestBetsConsumerService.builder()
        .commonPath("/foo/bar")
        .build());
  }

  @Test
  void testAddBestBet() {
    testClientBestBetsConsumerServiceMissingAction(
        clientBestBetsConsumerService -> clientBestBetsConsumerService.addBestBet(DefaultBestBetPayload.builder()
            .build()));
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().addBestBetAction(
        "/bestbet").build();
    assertDoesNotThrow(() -> properlyConfigured.addBestBet(
        DefaultBestBetPayload.builder().index("foo").language("de").term("bar").url("baz").build()));
  }

  @Test
  void testAddBestBet_failed() {
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().addBestBetAction(
        "/bestbet").build();
    DefaultBestBetPayload payload = DefaultBestBetPayload.builder().index("foo").language("de").term("bar").url("baz")
        .build();
    assertThrows(BestBetsActionFailedException.class, () -> properlyConfigured.addBestBet(payload));
  }

  @Test
  void testAddBestBets() {
    testClientBestBetsConsumerServiceMissingAction(
        clientBestBetsConsumerService -> clientBestBetsConsumerService.addBestBets(
            Collections.singletonList(DefaultBestBetPayload.builder()
                .build())));
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().addBestBetsAction(
        "/bestbets").build();
    assertDoesNotThrow(() -> properlyConfigured.addBestBets(Collections.singletonList(
        DefaultBestBetPayload.builder().index("foo").language("de").term("bar").url("baz").build())));
  }

  @Test
  void testAddBestBets_failed() {
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().addBestBetsAction(
        "/bestbet").build();
    List<BestBetPayload> payload = Collections.singletonList(
        DefaultBestBetPayload.builder().index("foo").language("de").term("bar").url("baz").build());
    assertThrows(BestBetsActionFailedException.class, () -> properlyConfigured.addBestBets(payload));
  }

  @Test
  void testUpdateBestBet() {
    testClientBestBetsConsumerServiceMissingAction(
        clientBestBetsConsumerService -> clientBestBetsConsumerService.updateBestBet(1, DefaultBestBetPayload.builder()
            .build()));
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(
                    getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
            .getAsJsonObject(), true)));
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().updateBestBetAction(
        "/bestbets").build();
    assertDoesNotThrow(() -> properlyConfigured.updateBestBet(1,
        DefaultBestBetPayload.builder().index("foo").language("de").term("bar").url("baz").build()));
  }

  @Test
  void testUpdateBestBet_failed() {
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().updateBestBetAction(
        "/bestbet").build();
    BestBetPayload payload = DefaultBestBetPayload.builder().index("foo").language("de").term("bar").url("baz").build();
    assertThrows(BestBetsActionFailedException.class, () -> properlyConfigured.updateBestBet(1, payload));
  }

  @Test
  void testDeleteBestBet() {
    testClientBestBetsConsumerServiceMissingAction(
        clientBestBetsConsumerService -> clientBestBetsConsumerService.deleteBestBet(1));
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(
                    getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
            .getAsJsonObject(), true)));
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().deleteBestBetAction(
        "/bestbets").build();
    assertDoesNotThrow(() -> properlyConfigured.deleteBestBet(1));
  }

  @Test
  void testDeleteBestBet_failed() {
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().deleteBestBetAction(
        "/bestbet").build();
    assertThrows(BestBetsActionFailedException.class, () -> properlyConfigured.deleteBestBet(1));
  }

  @Test
  void testPublishBestBetsForProject() {
    testClientBestBetsConsumerServiceMissingAction(
        clientBestBetsConsumerService -> clientBestBetsConsumerService.publishBestBetsForProject(1));
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().publishBestBetsForProjectAction(
        "/bestbets/%s/publish").build();
    assertDoesNotThrow(() -> properlyConfigured.publishBestBetsForProject(1));
    ClientBestBetsConsumerService improperlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().publishBestBetsForProjectAction(
        "/bestbets").build();
    assertThrows(IllegalArgumentException.class, () -> improperlyConfigured.publishBestBetsForProject(1));
  }

  @Test
  void testPublishBestBetsForProject_failed() {
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().publishBestBetsForProjectAction(
        "/bestbets/%s/publish").build();
    assertThrows(BestBetsActionFailedException.class, () -> properlyConfigured.publishBestBetsForProject(1));
  }

  @Test
  void testGetBestBets() {
    testClientBestBetsConsumerServiceMissingAction(
        ClientBestBetsConsumerService::getBestBets);
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/getBestBets.json")))
            .getAsJsonArray(), true)));
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().getBestBetsAction(
        "/bestbets").build();
    assertThat(properlyConfigured.getBestBets(), not(empty()));
  }

  @Test
  void testGetBestBets_failed() {
    ClientBestBetsConsumerService properlyConfigured = getConClientBestBetsConsumerServiceBuilderWithCommonConfig().getBestBetsAction(
        "/bestbets").build();
    assertThat(properlyConfigured.getBestBets(), empty());
  }

  private void testClientBestBetsConsumerServiceMissingAction(Consumer<ClientBestBetsConsumerService> consumer) {
    ClientBestBetsConsumerService clientBestBetsConsumerService =
        getConClientBestBetsConsumerServiceBuilderWithCommonConfig()
            .build();
    assertThrows(IllegalStateException.class, () -> consumer.accept(clientBestBetsConsumerService));

  }

  private ClientBestBetsConsumerService.ClientBestBetsConsumerServiceBuilder getConClientBestBetsConsumerServiceBuilderWithCommonConfig() {
    return ClientBestBetsConsumerService.builder()
        .searchRequestExecutorService(searchRequestExecutorService)
        .commonPath("/foo/bar");
  }


  private void testClientBestBetsConsumerServiceIllegalState(
      ClientBestBetsConsumerService clientBestBetsConsumerService) {
    assertThrows(IllegalStateException.class, clientBestBetsConsumerService::getBestBets);
  }
}
