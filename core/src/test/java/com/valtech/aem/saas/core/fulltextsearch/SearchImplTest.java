package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.text.IsEmptyString.isEmptyString;

import com.adobe.cq.export.json.ComponentExporter;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.Filter;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.Search;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Map;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchImplTest {

  private final AemContext context = new AemContextBuilder()
      .plugin(ContextPlugins.CACONFIG)
      .build();

  @Mock
  FulltextSearchService fulltextSearchService;

  @Mock
  FulltextSearchConfigurationService fulltextSearchConfigurationService;

  Search testee;

  @BeforeEach
  void setUp() {
    context.registerService(FulltextSearchConfigurationService.class, fulltextSearchConfigurationService);
    context.registerService(FulltextSearchService.class, fulltextSearchService);
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource("/content/saas-aem-module/us/en/jcr:content/root/container/container/search");
    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
  }

  @Test
  void testAdaptRequest() {
    MockContextAwareConfig.writeConfiguration(context, context.currentResource().getPath(), SearchConfiguration.class,
        "index", "foo");
    context.request().addRequestParameter(SearchResultsImpl.SEARCH_TERM, "bar");
    adaptRequest();
    testAdaptable();
    Map<String, ? extends ComponentExporter> exportedItemsMap = testee.getExportedItems();
    assertThat(exportedItemsMap.isEmpty(), is(true));
    assertThat(testee.getSearchButtonText(), is(SearchImpl.I18N_KEY_SEARCH_BUTTON_LABEL));
    assertThat(testee.getLoadMoreButtonText(), is(SearchResultsImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL));
    assertThat(testee.getResultsPerPage(), is(15));
    assertThat(testee.getSearchFieldPlaceholderText(), is("Type search term here..."));
    assertThat(testee.getFilters(), emptyCollectionOf(Filter.class));
    assertThat(testee.getAutocompleteTriggerThreshold(), is(3));
    assertThat(testee.getTerm(), is("bar"));
  }

  @Test
  void testAdaptResource() {
    adaptResource();
    testAdaptable();
    Map<String, ? extends ComponentExporter> exportedItemsMap = testee.getExportedItems();
    assertThat(exportedItemsMap.isEmpty(), is(true));
    String[] order = testee.getExportedItemsOrder();
    assertThat(order, is(notNullValue()));
    assertThat(order.length, is(0));
    assertThat(testee.getResultsPerPage(), is(15));
    assertThat(testee.getSearchFieldPlaceholderText(), is("Type search term here..."));
    assertThat(testee.getFilters(), nullValue());
    assertThat(testee.getAutocompleteTriggerThreshold(), is(SearchImpl.AUTOCOMPLETE_THRESHOLD));
    assertThat(testee.getTerm(), is(nullValue()));
    assertThat(testee.getSearchButtonText(), isEmptyString());
    assertThat(testee.getLoadMoreButtonText(), isEmptyString());

  }

  private void adaptRequest() {
    testee = context.request().adaptTo(Search.class);
  }

  private void adaptResource() {
    testee = context.currentResource().adaptTo(Search.class);
  }

  private void testAdaptable() {
    assertThat(testee, notNullValue());
    assertThat(testee, instanceOf(Search.class));
  }
}
