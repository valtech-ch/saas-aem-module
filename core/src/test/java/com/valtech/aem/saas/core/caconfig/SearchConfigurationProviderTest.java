package com.valtech.aem.saas.core.caconfig;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchConfigurationProviderTest {

  private final AemContext context = new AemContextBuilder()
      .plugin(ContextPlugins.CACONFIG)
      .build();

  @Mock
  Resource resource;

  @Test
  void testConfigurationBuilderNotAvailable() {
    assertThrows(IllegalArgumentException.class, () -> new SearchConfigurationProvider(resource));
  }


  @Test
  void testConfiguration() {
    setUpCaConfig(context);
    Resource contextResource = context.currentResource();
    MockContextAwareConfig.writeConfiguration(context, contextResource.getPath(), SearchConfiguration.class,
        "index", "foo", "client", "bar");
    SearchConfigurationProvider searchConfigurationProvider = new SearchConfigurationProvider(contextResource);
    assertThat(searchConfigurationProvider.getIndex(), is("foo"));
    assertThat(searchConfigurationProvider.getClient(), is("bar"));
    assertThat(searchConfigurationProvider.getFilters(), empty());
    assertThat(searchConfigurationProvider.getTemplates(), empty());
    assertThat(searchConfigurationProvider.isBestBetsEnabled(), is(false));
    assertThat(searchConfigurationProvider.isAutoSuggestEnabled(), is(true));

  }

  @Test
  void testConfiguration_missingRequiredFields() {
    setUpCaConfig(context);
    Resource contextResource = context.currentResource();
    SearchConfigurationProvider searchConfigurationProvider = new SearchConfigurationProvider(contextResource);
    assertThrows(IllegalStateException.class, searchConfigurationProvider::getIndex);
    assertThrows(IllegalStateException.class, searchConfigurationProvider::getClient);
  }

  private void setUpCaConfig(AemContext context) {
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource("/content/saas-aem-module/us/en/jcr:content");
    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
  }
}
