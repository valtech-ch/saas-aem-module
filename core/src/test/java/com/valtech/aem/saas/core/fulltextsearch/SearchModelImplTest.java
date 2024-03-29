package com.valtech.aem.saas.core.fulltextsearch;

import com.adobe.cq.export.json.ComponentExporter;
import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchPingService;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.hamcrest.collection.IsEmptyCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchModelImplTest {

    private final AemContext context = new AemContextBuilder()
            .plugin(ContextPlugins.CACONFIG)
            .build();

    @Mock
    FulltextSearchPingService fulltextSearchPingService;

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
        when(i18n.get(SearchModelImpl.I18N_SEARCH_SUGGESTION_TEXT)).thenReturn("Did you mean");
        when(i18n.get(SearchModelImpl.I18N_SEARCH_NO_RESULTS_TEXT)).thenReturn("No results.");

        context.registerService(FulltextSearchPingService.class, fulltextSearchPingService);
        context.registerService(PathTransformer.class, pathTransformer);
        context.registerService(I18nProvider.class, i18nProvider);
        context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
        context.create().page("/content/saas-aem-module/us");
        context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
        context.currentPage("/content/saas-aem-module/us/en");
        context.currentResource("/content/saas-aem-module/us/en/jcr:content/root/container/container/search");
        context.requestPathInfo()
               .setResourcePath("/content/saas-aem-module/us/en/jcr:content/root/container/container/search");
        MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
    }

    @Test
    void testAdaptRequest() {
        when(i18n.get(SearchModelImpl.I18N_SEARCH_CONNECTION_FAILED_FURTHER_ACTION_CHECK_OSGI_CONFIGURATION)).thenReturn(
                "Check osgi configs.");
        when(i18n.get(SearchModelImpl.I18N_SEARCH_CONNECTION_FAILED_FURTHER_ACTION_CHECK_LOG_FILES)).thenReturn(
                "Check log files");
        when(pathTransformer.map(any(SlingHttpServletRequest.class),
                                 eq("/content/saas-aem-module/us/en/jcr:content/root/container/container/search" +
                                            "/search-tabs/searchtab"))).thenReturn(
                "foo");
        when(pathTransformer.map(any(SlingHttpServletRequest.class),
                                 eq("/content/saas-aem-module/us/en/jcr:content/root/container/container/search" +
                                            "/search-tabs/searchtab_2"))).thenReturn(
                "bar");
        when(pathTransformer.map(any(SlingHttpServletRequest.class),
                                 eq("/content/saas-aem-module/us/en/jcr:content/root/container/container/search"))).thenReturn(
                "/search");
        MockContextAwareConfig.writeConfiguration(context,
                                                  context.currentResource().getPath(),
                                                  SearchConfiguration.class,
                                                  "index",
                                                  "foo",
                                                  "enableSearchResultItemTracking",
                                                  true);
        context.request().addRequestParameter(SearchTabModel.QUERY_PARAM_SEARCH_TERM, "bar");
        adaptRequest();
        testAdaptable();
        Map<String, ? extends ComponentExporter> exportedItemsMap = testee.getExportedItems();
        assertThat(exportedItemsMap.isEmpty(), is(true));
        assertThat(testee.getSearchButtonText(), is("search"));
        assertThat(testee.getLoadMoreButtonText(), is("load more"));
        assertThat(testee.getResultsPerPage(), is(15));
        assertThat(testee.getSearchFieldPlaceholderText(), is("Type search term here..."));
        assertThat(testee.getFilters(), IsEmptyCollection.empty());
        assertThat(testee.getAutocompleteTriggerThreshold(), is(3));
        assertThat(testee.getSearchTabs(), not(empty()));
        assertThat(testee.getAutocompleteUrl(), is("/search.autocomplete.json"));
        assertThat(testee.getTrackingUrl(), is("/search.tracking.json"));
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
        assertThat(testee.getFilters(), IsEmptyCollection.empty());
        assertThat(testee.getAutocompleteTriggerThreshold(),
                   is(SearchConfiguration.DEFAULT_AUTOCOMPLETE_TRIGGER_THRESHOLD));
        assertThat(testee.getId(), not(isEmptyString()));
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
