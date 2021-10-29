package com.valtech.aem.saas.core.bestbets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.bestbets.BestBetsActionFailedException;
import com.valtech.aem.saas.api.bestbets.dto.BestBetPayloadDTO;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultBestBetsServiceTest {

  private final AemContext context = new AemContextBuilder()
      .plugin(ContextPlugins.CACONFIG)
      .build();

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  @Mock
  SearchRequestExecutorService searchRequestExecutorService;

  DefaultBestBetsService testee;

  @BeforeEach
  void setUp() {
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource("/content/saas-aem-module/us/en/jcr:content");
    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    context.registerService(SearchRequestExecutorService.class, searchRequestExecutorService);
    testee = context.registerInjectActivateService(new DefaultBestBetsService());
  }

  @Test
  void testAddBestBet() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    assertDoesNotThrow(() -> testee.addBestBet(contextResource,
        new BestBetPayloadDTO("foo", "baz", "de")));
  }

  @Test
  void testClientMissing() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo");
    BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
    assertThrows(IllegalStateException.class, () -> testee.addBestBet(contextResource, payload));
    List<BestBetPayloadDTO> bestBetsPayload = Collections.singletonList(payload);
    assertThrows(IllegalStateException.class, () -> testee.addBestBets(contextResource, bestBetsPayload));
    assertThrows(IllegalStateException.class, () -> testee.updateBestBet(contextResource, 1,
        payload));
    assertThrows(IllegalStateException.class, () -> testee.deleteBestBet(contextResource, 1));
    assertThrows(IllegalStateException.class, () -> testee.publishBestBetsForProject(contextResource, 1));
    assertThrows(IllegalStateException.class, () -> testee.getBestBets(contextResource));
  }

  @Test
  void testIndexMissing() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "client", "bar");
    BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
    List<BestBetPayloadDTO> bestBetsPayload = Collections.singletonList(payload);
    assertThrows(IllegalStateException.class, () -> testee.addBestBet(contextResource,
        payload));
    assertThrows(IllegalStateException.class, () -> testee.addBestBets(contextResource, bestBetsPayload));
    assertThrows(IllegalStateException.class, () -> testee.updateBestBet(contextResource, 1,
        payload));
  }

  @Test
  void testAddBestBet_failed() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
    assertThrows(BestBetsActionFailedException.class, () -> testee.addBestBet(contextResource, payload));
  }

  @Test
  void testAddBestBets() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    assertDoesNotThrow(() -> testee.addBestBets(contextResource, Collections.singletonList(
        new BestBetPayloadDTO("foo", "baz", "de"))));
  }

  @Test
  void testAddBestBets_failed() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    List<BestBetPayloadDTO> payload = Collections.singletonList(
        new BestBetPayloadDTO("foo", "baz", "de"));
    assertThrows(BestBetsActionFailedException.class, () -> testee.addBestBets(contextResource, payload));
  }

  @Test
  void testUpdateBestBet() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(
                    getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
            .getAsJsonObject(), true)));
    assertDoesNotThrow(() -> testee.updateBestBet(contextResource, 1,
        new BestBetPayloadDTO("foo", "baz", "de")));
  }

  @Test
  void testUpdateBestBet_missingAction(AemContext context) {
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiUpdateBestBetAction", "")
            .build());
    Resource contextResource = context.currentResource();
    BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
    assertThrows(IllegalStateException.class, () -> testee.updateBestBet(contextResource, 1,
        payload));
  }

  @Test
  void testUpdateBestBet_failed() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
    assertThrows(BestBetsActionFailedException.class, () -> testee.updateBestBet(contextResource, 1, payload));
  }

  @Test
  void testDeleteBestBet() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(
                    getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
            .getAsJsonObject(), true)));
    assertDoesNotThrow(() -> testee.deleteBestBet(contextResource, 1));
  }

  @Test
  void testDeleteBestBet_missingAction(AemContext context) {
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiDeleteBestBetAction", "")
            .build());
    Resource contextResource = context.currentResource();
    assertThrows(IllegalStateException.class, () -> testee.deleteBestBet(contextResource, 1));
  }

  @Test
  void testDeleteBestBet_failed() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    assertThrows(BestBetsActionFailedException.class, () -> testee.deleteBestBet(contextResource, 1));
  }

  @Test
  void testPublishBestBetsForProject() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    assertDoesNotThrow(() -> testee.publishBestBetsForProject(contextResource, 1));
  }

  @Test
  void testPublishBestBetsForProject_wrongConfigForAction(AemContext context) {
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiPublishProjectBestBetsAction", "/bestbets")
            .build());
    Resource contextResource = context.currentResource();
    assertThrows(IllegalArgumentException.class, () -> testee.publishBestBetsForProject(contextResource, 1));
  }

  @Test
  void testPublishBestBetsForProject_failed() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    assertThrows(BestBetsActionFailedException.class, () -> testee.publishBestBetsForProject(contextResource, 1));
  }

  @Test
  void testGetBestBets() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    Mockito.when(searchRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/getBestBets.json")))
            .getAsJsonArray(), true)));
    assertThat(testee.getBestBets(contextResource), not(empty()));
  }

  @Test
  void testGetBestBets_missingAction(AemContext context) {
    Resource contextResource = context.currentResource();
    testee = context.registerInjectActivateService(new DefaultBestBetsService(),
        ImmutableMap.<String, Object>builder()
            .put("bestBetsService.apiGetAllBestBetsAction", "")
            .build());
    assertThrows(IllegalStateException.class, () -> testee.getBestBets(contextResource));
  }

  @Test
  void testGetBestBets_failed() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    assertThrows(BestBetsActionFailedException.class, () -> testee.getBestBets(contextResource));
  }

}
