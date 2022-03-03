package com.valtech.aem.saas.core.tracking;

import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.api.tracking.TrackingService;
import com.valtech.aem.saas.api.tracking.dto.SearchResultItemTrackingDTO;
import com.valtech.aem.saas.core.fulltextsearch.SearchModelImpl;
import com.valtech.aem.saas.core.fulltextsearch.SearchTabModelImpl;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchResultItemTrackingServletTest {

    private final AemContext context = new AemContextBuilder()
            .plugin(ContextPlugins.CACONFIG)
            .build();

    @Mock
    TrackingService trackingService;

    @Mock
    I18nProvider i18nProvider;

    @Mock
    PathTransformer pathTransformer;

    @Mock
    I18n i18n;

    SearchResultItemTrackingServlet testee;

    @BeforeEach
    void setUp() {
        context.registerService(TrackingService.class, trackingService);
        context.registerService(I18nProvider.class, i18nProvider);
        context.registerService(PathTransformer.class, pathTransformer);
        testee = context.registerInjectActivateService(new SearchResultItemTrackingServlet());
        context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
        context.create().page("/content/saas-aem-module/us");
        context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
        context.currentPage("/content/saas-aem-module/us/en");
        context.currentResource("/content/saas-aem-module/us/en/jcr:content/root/container/container/search");
        MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
        context.requestPathInfo().setSelectorString(SearchResultItemTrackingServlet.SEARCH_RESULT_ITEM_TRACKING_SELECTOR);
    }

    @Test
    void testTracking_noTrackedUrl() throws ServletException, IOException {
        SlingHttpServletRequest request = context.request();
        SlingHttpServletResponse response = context.response();
        testee.doPost(request, response);
        assertThat(response.getStatus(), is(HttpServletResponse.SC_BAD_REQUEST));
    }

    @Test
    void testTracking() throws ServletException, IOException {
        MockContextAwareConfig.writeConfiguration(context,
                                                  context.currentResource().getPath(),
                                                  SearchConfiguration.class,
                                                  "enableSearchResultItemTracking",
                                                  true);
        when(i18nProvider.getI18n(Locale.ENGLISH)).thenReturn(i18n);
        when(i18n.get(SearchTabModelImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL)).thenReturn("load more");
        when(i18n.get(SearchModelImpl.I18N_KEY_SEARCH_BUTTON_LABEL)).thenReturn("search");
        when(i18n.get(SearchModelImpl.I18N_SEARCH_SUGGESTION_TEXT)).thenReturn("Did you mean");
        when(i18n.get(SearchModelImpl.I18N_SEARCH_NO_RESULTS_TEXT)).thenReturn("No results.");
        when(trackingService.trackUrl(anyString())).thenReturn(Optional.of(new SearchResultItemTrackingDTO("", "", 0, "")));
        when(pathTransformer.map(any(SlingHttpServletRequest.class), Mockito.eq(context.currentResource().getPath()))).thenReturn(context.currentResource().getPath());
        SlingHttpServletRequest request = context.request();
        SlingHttpServletResponse response = context.response();
        context.request().addRequestParameter(SearchResultItemTrackingServlet.QUERY_PARAM_TRACKED_URL, "foo");
        testee.doPost(request, response);
        assertThat(response.getStatus(), is(HttpServletResponse.SC_CREATED));
    }
}
