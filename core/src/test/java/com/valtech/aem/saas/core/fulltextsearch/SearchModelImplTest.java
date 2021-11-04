package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import com.adobe.cq.export.json.ComponentExporter;
import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Locale;
import java.util.Map;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchModelImplTest {

  private final AemContext context = new AemContextBuilder()
      .plugin(ContextPlugins.CACONFIG)
      .build();

  @Mock
  FulltextSearchService fulltextSearchService;

  @Mock
  PathTransformer pathTransformer;

  @Mock
  I18nProvider i18nProvider;

  @Mock
  I18n i18n;

  SearchModelImpl testee;

  @BeforeEach
  void setUp() {
    when(i18nProvider.getI18n(Locale.ENGLISH)).thenReturn(i18n);
    when(i18n.get(SearchTabModelImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL)).thenReturn("load more");
    when(i18n.get(SearchModelImpl.I18N_KEY_SEARCH_BUTTON_LABEL)).thenReturn("search");
    context.registerService(FulltextSearchService.class, fulltextSearchService);
    context.registerService(PathTransformer.class, pathTransformer);
    context.registerService(I18nProvider.class, i18nProvider);
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource("/content/saas-aem-module/us/en/jcr:content/root/container/container/search");
    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
  }

  @Test
  void testAdaptRequest() {
    when(pathTransformer.map(any(SlingHttpServletRequest.class),
        eq("/content/saas-aem-module/us/en/jcr:content/root/container/container/search/search-tabs/searchtab"))).thenReturn(
        "foo");
    when(pathTransformer.map(any(SlingHttpServletRequest.class),
        eq("/content/saas-aem-module/us/en/jcr:content/root/container/container/search/search-tabs/searchtab_2"))).thenReturn(
        "bar");
    when(pathTransformer.map(any(SlingHttpServletRequest.class),
        eq("/content/saas-aem-module/us/en/jcr:content/root/container/container/search"))).thenReturn(
        "/search");
    MockContextAwareConfig.writeConfiguration(context, context.currentResource().getPath(), SearchConfiguration.class,
        "index", "foo");
    context.request().addRequestParameter(SearchTabModelImpl.SEARCH_TERM, "bar");
    adaptRequest();
    testAdaptable();
    Map<String, ? extends ComponentExporter> exportedItemsMap = testee.getExportedItems();
    assertThat(exportedItemsMap.isEmpty(), is(true));
    assertThat(testee.getSearchButtonText(), is("search"));
    assertThat(testee.getLoadMoreButtonText(), is("load more"));
    assertThat(testee.getResultsPerPage(), is(15));
    assertThat(testee.getSearchFieldPlaceholderText(), is("Type search term here..."));
    assertThat(testee.getFilters(), nullValue());
    assertThat(testee.getAutocompleteTriggerThreshold(), is(3));
    assertThat(testee.getSearchTabs(), not(empty()));
    assertThat(testee.getAutosuggestUrl(), is("/search"));
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
    assertThat(testee.getAutocompleteTriggerThreshold(), is(SearchModelImpl.AUTOCOMPLETE_THRESHOLD));
  }

  private void adaptRequest() {
    testee = context.request().adaptTo(SearchModelImpl.class);
  }

  private void adaptResource() {
    testee = context.currentResource().adaptTo(SearchModelImpl.class);
  }

  private void testAdaptable() {
    assertThat(testee, notNullValue());
    assertThat(testee, instanceOf(SearchModelImpl.class));
  }

}
