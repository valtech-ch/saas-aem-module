package com.valtech.aem.saas.core.tracking;

import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.autocomplete.AutocompleteService;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.query.SimpleFilter;
import com.valtech.aem.saas.api.tracking.TrackingService;
import com.valtech.aem.saas.api.tracking.dto.UrlTrackingDTO;
import com.valtech.aem.saas.core.autocomplete.DefaultAutocompleteService;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchAdminRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchApiRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.i18n.I18nProvider;
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
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultTrackingServiceTest {

    private final AemContext context = new AemContextBuilder()
            .plugin(ContextPlugins.CACONFIG)
            .build();

    @Mock
    SearchAdminRequestExecutorService searchAdminRequestExecutorService;

    @Mock
    HttpClientBuilderFactory httpClientBuilderFactory;

    Resource currentResource;

    SearchCAConfigurationModel searchCAConfigurationModel;

    TrackingService service;

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
        context.registerService(SearchAdminRequestExecutorService.class, searchAdminRequestExecutorService);
        service = context.registerInjectActivateService(new DefaultTrackingService());
        currentResource = context.currentResource();
    }

    @Test
    void testInputValidation_noUrl() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        assertThrows(NullPointerException.class,
                     () -> service.trackUrl(null));
        assertThrows(IllegalArgumentException.class,
                     () -> service.trackUrl(""));
    }


    @Test
    void testTrackUrl() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        when(searchAdminRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                        "/__files/search/tracking/success.json"))).getAsJsonObject(), true)));
        Optional<UrlTrackingDTO> result = service.trackUrl("https://www.valtech.com/career/jobs/business-development-lead-1569700");
        assertThat(result.isPresent(), is(true));
    }

    @Test
    void testTrackUrl_unexpectedResponseContent() {
        searchCAConfigurationModel = currentResource.adaptTo(SearchCAConfigurationModel.class);
        when(searchAdminRequestExecutorService.execute(Mockito.any(SearchRequest.class))).thenReturn(
                Optional.of(new SearchResponse(new JsonParser().parse(""), true)));
        Optional<UrlTrackingDTO> result = service.trackUrl("https://www.valtech.com/career/jobs/business-development-lead-1569700");
        assertThat(result.isPresent(), is(false));
    }
}
