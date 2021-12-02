package com.valtech.aem.saas.core.fulltextsearch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.InputStreamReader;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultFulltextSearchServiceTest {

    private final AemContext context = new AemContextBuilder()
            .plugin(ContextPlugins.CACONFIG)
            .build();

    @Mock
    HttpClientBuilderFactory httpClientBuilderFactory;

    @Mock
    SearchRequestExecutorService searchRequestExecutorService;

    FulltextSearchService testee;

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
        context.registerService(SearchRequestExecutorService.class, searchRequestExecutorService);
        testee = context.registerInjectActivateService(new DefaultFulltextSearchService());
        currentResource = context.currentResource();
    }

    @Test
    void testNullArguments() {
        Assertions.assertThrows(NullPointerException.class, () -> testee.getResults(null, "de", 0, 10));
    }

    @Test
    void testSearchIndexNotConfigured() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        Assertions.assertThrows(IllegalStateException.class,
                                () -> testee.getResults(searchCAConfigurationModel, "de", 0, 10));
    }

    @Test
    void testGetResults_failedRequestExecution() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(Optional.empty());
        assertThat(testee.getResults(searchCAConfigurationModel, "de", 0, 10).isPresent(), is(false));
    }

    @Test
    void testGetResults_responseBodyMissing() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonObject(), true)));
        assertThat(testee.getResults(searchCAConfigurationModel, "de", 0, 10).isPresent(), is(false));
    }

    @Test
    void testGetResults_ok() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(
                                                                       new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
                                                               .getAsJsonObject(), true)));
        assertThat(testee.getResults(searchCAConfigurationModel, "de", 0, 10).isPresent(), is(true));
    }

}
