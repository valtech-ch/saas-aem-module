package com.valtech.aem.saas.core.typeahead;

import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.query.SimpleFilter;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchApiRequestExecutorService;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
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

import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultTypeaheadServiceTest {

    private final AemContext context = new AemContextBuilder()
            .plugin(ContextPlugins.CACONFIG)
            .build();

    @Mock
    SearchApiRequestExecutorService searchApiRequestExecutorService;

    @Mock
    HttpClientBuilderFactory httpClientBuilderFactory;

    TypeaheadService service;

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
        service = context.registerInjectActivateService(new DefaultTypeaheadService());
        currentResource = context.currentResource();
    }

    @Test
    void testInputValidation_noIndex() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        assertThrows(IllegalStateException.class,
                     () -> service.getResults(searchCAConfigurationModel, "foo", "de", null));
    }

    @Test
    void testInputValidation_noText() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        assertThrows(IllegalStateException.class,
                     () -> service.getResults(searchCAConfigurationModel, "foo", "de", null));
    }

    @Test
    void testGetResults() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(
                                new InputStreamReader(getClass().getResourceAsStream("/__files/search/typeahead/success.json")))
                        .getAsJsonObject(), true)));
        assertThat(service.getResults(searchCAConfigurationModel, "foo bar", "en",
                                      new HashSet<>(Collections.singletonList(new SimpleFilter("foo", "bar")))),
                   is(not(empty())));
    }

    @Test
    void testGetResults_noTypeaheadOptions() {
        MockContextAwareConfig.writeConfiguration(context, currentResource.getPath(), SearchConfiguration.class,
                                                  "index", "bar");
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        when(searchApiRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(
                                new InputStreamReader(getClass().getResourceAsStream("/__files/search/typeahead/empty.json")))
                        .getAsJsonObject(), true)));
        assertThat(service.getResults(searchCAConfigurationModel, "foo bar", "en",
                                      new HashSet<>(Collections.singletonList(new SimpleFilter("foo", "bar")))),
                   is(empty()));
    }

}
