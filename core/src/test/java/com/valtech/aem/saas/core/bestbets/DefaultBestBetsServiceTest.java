package com.valtech.aem.saas.core.bestbets;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.bestbets.BestBetsActionFailedException;
import com.valtech.aem.saas.api.bestbets.dto.BestBetPayloadDTO;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchApiRequestExecutorService;
import com.valtech.aem.saas.core.http.response.BestBetIdDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultBestBetsServiceTest {

    private final AemContext context = new AemContextBuilder()
            .plugin(ContextPlugins.CACONFIG)
            .build();

    @Mock
    HttpClientBuilderFactory httpClientBuilderFactory;

    @Mock
    SearchApiRequestExecutorService searchApiRequestExecutorService;

    DefaultBestBetsService testee;

    Resource currentResource;

    SearchCAConfigurationModel searchCAConfigurationModel;

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
        context.registerService(SearchApiRequestExecutorService.class, searchApiRequestExecutorService);
        testee = context.registerInjectActivateService(new DefaultBestBetsService());
        currentResource = context.currentResource();
    }

    @Test
    void testAddBestBet() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        JsonObject bestBetAddedResponse = new JsonObject();
        bestBetAddedResponse.addProperty(BestBetIdDataExtractionStrategy.ID, 1);
        Mockito.when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(bestBetAddedResponse, true)));
        assertThat(testee.addBestBet(searchCAConfigurationModel,
                                     new BestBetPayloadDTO("foo", "baz", "de")),
                   Is.is(1));
    }

    @Test
    void testClientMissing() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
        assertThrows(IllegalStateException.class, () -> testee.addBestBet(searchCAConfigurationModel, payload));
        List<BestBetPayloadDTO> bestBetsPayload = Collections.singletonList(payload);
        assertThrows(IllegalStateException.class,
                     () -> testee.addBestBets(searchCAConfigurationModel, bestBetsPayload));
        assertThrows(IllegalStateException.class, () -> testee.updateBestBet(searchCAConfigurationModel, 1,
                                                                             payload));
        assertThrows(IllegalStateException.class, () -> testee.deleteBestBet(searchCAConfigurationModel, 1));
        assertThrows(IllegalStateException.class,
                     () -> testee.publishBestBetsForProject(searchCAConfigurationModel, 1));
        assertThrows(IllegalStateException.class, () -> testee.getBestBets(searchCAConfigurationModel));
    }

    @Test
    void testIndexMissing() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
        List<BestBetPayloadDTO> bestBetsPayload = Collections.singletonList(payload);
        assertThrows(IllegalStateException.class, () -> testee.addBestBet(searchCAConfigurationModel,
                                                                          payload));
        assertThrows(IllegalStateException.class,
                     () -> testee.addBestBets(searchCAConfigurationModel, bestBetsPayload));
        assertThrows(IllegalStateException.class, () -> testee.updateBestBet(searchCAConfigurationModel, 1,
                                                                             payload));
    }

    @Test
    void testAddBestBet_failed() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
        assertThrows(BestBetsActionFailedException.class, () -> testee.addBestBet(searchCAConfigurationModel, payload));
    }

    @Test
    void testAddBestBets() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        Mockito.when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonObject(), true)));
        assertDoesNotThrow(() -> testee.addBestBets(searchCAConfigurationModel, Collections.singletonList(
                new BestBetPayloadDTO("foo", "baz", "de"))));
    }

    @Test
    void testAddBestBets_failed() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        List<BestBetPayloadDTO> payload = Collections.singletonList(
                new BestBetPayloadDTO("foo", "baz", "de"));
        assertThrows(BestBetsActionFailedException.class,
                     () -> testee.addBestBets(searchCAConfigurationModel, payload));
    }

    @Test
    void testUpdateBestBet() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        Mockito.when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(
                                new InputStreamReader(
                                        getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
                        .getAsJsonObject(), true)));
        assertDoesNotThrow(() -> testee.updateBestBet(searchCAConfigurationModel, 1,
                                                      new BestBetPayloadDTO("foo", "baz", "de")));
    }

    @Test
    void testUpdateBestBet_missingAction() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        testee = context.registerInjectActivateService(new DefaultBestBetsService(),
                                                       ImmutableMap.<String, Object>builder()
                                                                   .put("bestBetsService.apiUpdateBestBetAction", "")
                                                                   .build());
        BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
        assertThrows(IllegalStateException.class, () -> testee.updateBestBet(searchCAConfigurationModel, 1,
                                                                             payload));
    }

    @Test
    void testUpdateBestBet_failed() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        BestBetPayloadDTO payload = new BestBetPayloadDTO("foo", "baz", "de");
        assertThrows(BestBetsActionFailedException.class,
                     () -> testee.updateBestBet(searchCAConfigurationModel, 1, payload));
    }

    @Test
    void testDeleteBestBet() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        Mockito.when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(
                                new InputStreamReader(
                                        getClass().getResourceAsStream("/__files/search/bestbets/modifiedBestBetResponse.json")))
                        .getAsJsonObject(), true)));
        assertDoesNotThrow(() -> testee.deleteBestBet(searchCAConfigurationModel, 1));
    }

    @Test
    void testDeleteBestBet_missingAction() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        testee = context.registerInjectActivateService(new DefaultBestBetsService(),
                                                       ImmutableMap.<String, Object>builder()
                                                                   .put("bestBetsService.apiDeleteBestBetAction", "")
                                                                   .build());
        assertThrows(IllegalStateException.class, () -> testee.deleteBestBet(searchCAConfigurationModel, 1));
    }

    @Test
    void testDeleteBestBet_failed() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        assertThrows(BestBetsActionFailedException.class, () -> testee.deleteBestBet(searchCAConfigurationModel, 1));
    }

    @Test
    void testPublishBestBetsForProject() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        Mockito.when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonObject(), true)));
        assertDoesNotThrow(() -> testee.publishBestBetsForProject(searchCAConfigurationModel, 1));
    }

    @Test
    void testPublishBestBetsForProject_wrongConfigForAction() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        testee = context.registerInjectActivateService(new DefaultBestBetsService(),
                                                       ImmutableMap.<String, Object>builder()
                                                                   .put("bestBetsService.apiPublishProjectBestBetsAction",
                                                                        "/bestbets")
                                                                   .build());
        assertThrows(IllegalArgumentException.class,
                     () -> testee.publishBestBetsForProject(searchCAConfigurationModel, 1));
    }

    @Test
    void testPublishBestBetsForProject_failed() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        assertThrows(BestBetsActionFailedException.class,
                     () -> testee.publishBestBetsForProject(searchCAConfigurationModel, 1));
    }

    @Test
    void testGetBestBets() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        Mockito.when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(
                                new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/getBestBets.json")))
                        .getAsJsonArray(), true)));
        assertThat(testee.getBestBets(searchCAConfigurationModel), not(empty()));
    }

    @Test
    void testGetBestBets_missingAction() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        testee = context.registerInjectActivateService(new DefaultBestBetsService(),
                                                       ImmutableMap.<String, Object>builder()
                                                                   .put("bestBetsService.apiGetAllBestBetsAction", "")
                                                                   .build());
        assertThrows(IllegalStateException.class, () -> testee.getBestBets(searchCAConfigurationModel));
    }

    @Test
    void testGetBestBets_failed() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "foo", "client", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        assertThrows(BestBetsActionFailedException.class, () -> testee.getBestBets(searchCAConfigurationModel));
    }

}
