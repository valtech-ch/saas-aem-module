package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.Result;
import com.valtech.aem.saas.api.fulltextsearch.SearchTab;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchTabImplTest {

  private final AemContext context = new AemContextBuilder()
      .plugin(ContextPlugins.CACONFIG)
      .build();

  @Mock
  FulltextSearchService fulltextSearchService;

  @Mock
  FulltextSearchConfigurationService fulltextSearchConfigurationService;

  SearchTab testee;

  @BeforeEach
  void setUp() {
    context.registerService(FulltextSearchConfigurationService.class, fulltextSearchConfigurationService);
    context.registerService(FulltextSearchService.class, fulltextSearchService);
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource(
        "/content/saas-aem-module/us/en/jcr:content/root/container/container/search/search-tabs/searchtab");
    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
  }

  @Test
  void testSearchResults() {
    MockContextAwareConfig.writeConfiguration(context, context.currentResource().getPath(), SearchConfiguration.class,
        "index", "foo");
    context.request().addRequestParameter(SearchTabImpl.SEARCH_TERM, "bar");
    testAdaptable();
    assertThat(testee.getLoadMoreButtonText(), is(SearchTabImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL));
    assertThat(testee.getResults(), emptyCollectionOf(Result.class));
    assertThat(testee.getResultsPerPage(), is(15));
    assertThat(testee.getTerm(), is("bar"));
    assertThat(testee.getResultsTotal(), is(0));
    assertThat(testee.getStartPage(), is(0));
    assertThat(testee.getSuggestion(), is(nullValue()));
    assertThat(testee.getExportedType(), is(SearchTabImpl.RESOURCE_TYPE));
  }

  private void testAdaptable() {
    testee = context.request().adaptTo(SearchTab.class);
    assertThat(testee, notNullValue());
    assertThat(testee, instanceOf(SearchTab.class));
  }
}
