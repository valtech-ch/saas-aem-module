package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.util.Locale;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchTabModelImplTest {

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

  SearchTabModel testee;

  @BeforeEach
  void setUp() {
    when(i18nProvider.getI18n(Locale.ENGLISH)).thenReturn(i18n);
    when(i18n.get(SearchTabModelImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL)).thenReturn("load more");
    when(i18n.get(SearchModelImpl.I18N_KEY_SEARCH_BUTTON_LABEL)).thenReturn("search");
    context.registerService(FulltextSearchService.class, fulltextSearchService);
    context.registerService(I18nProvider.class, i18nProvider);
    context.registerService(PathTransformer.class, pathTransformer);
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
    when(pathTransformer.map(any(SlingHttpServletRequest.class),
        eq("/content/saas-aem-module/us/en/jcr:content/root/container/container/search/search-tabs/searchtab"))).thenReturn(
        "foo");
    MockContextAwareConfig.writeConfiguration(context, context.currentResource().getPath(), SearchConfiguration.class,
        "index", "foo");
    context.requestPathInfo().setResourcePath(
        "/content/saas-aem-module/us/en/jcr:content/root/container/container/search/search-tabs/searchtab");
    context.request().addRequestParameter(SearchTabModel.SEARCH_TERM, "bar");
    context.request().addRequestParameter(SearchTabModel.QUERY_PARAM_START, "2");
    ArgumentCaptor<Integer> startParam = ArgumentCaptor.forClass(Integer.class);
    testAdaptable();
    verify(fulltextSearchService).getResults(any(SearchCAConfigurationModel.class), anyString(), anyString(),
        startParam.capture(), anyInt(), anySet(), anySet());
    assertThat(testee.getResults(), nullValue());
    assertThat(testee.getResultsTotal(), is(0));
    assertThat(testee.getSuggestion(), is(nullValue()));
    assertThat(testee.getExportedType(), is(SearchTabModelImpl.RESOURCE_TYPE));
    assertThat(startParam.getValue(), Is.is(15));
  }

  private void testAdaptable() {
    testee = context.request().adaptTo(SearchTabModel.class);
    assertThat(testee, notNullValue());
    assertThat(testee, instanceOf(SearchTabModel.class));
  }
}
