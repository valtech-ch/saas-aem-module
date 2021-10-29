package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.InputStreamReader;
import java.util.Optional;
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
  }

  @Test
  void testNullArguments() {
    Assertions.assertThrows(NullPointerException.class, () -> testee.getResults(null, 0, 10));
  }

  @Test
  void testSearchIndexNotConfigured() {
    Resource contextResource = context.currentResource();
    Assertions.assertThrows(IllegalStateException.class, () -> testee.getResults(contextResource, 0, 10));
  }

  @Test
  void testGetResults_failedRequestExecution() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "bar");
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(Optional.empty());
    assertThat(testee.getResults(contextResource, 0, 10).isPresent(), is(false));
  }

  @Test
  void testGetResults_responseBodyMissing() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "bar");
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonObject(), true)));
    assertThat(testee.getResults(contextResource, 0, 10).isPresent(), is(false));
  }

  @Test
  void testGetResults_ok() {
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "bar");
    when(searchRequestExecutorService.execute(any(SearchRequest.class))).thenReturn(
        Optional.of(new SearchResponse(new JsonParser().parse(
                new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
            .getAsJsonObject(), true)));
    assertThat(testee.getResults(contextResource, 0, 10).isPresent(), is(true));
  }

}
