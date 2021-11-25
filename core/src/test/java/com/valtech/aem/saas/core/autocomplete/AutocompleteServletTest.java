package com.valtech.aem.saas.core.autocomplete;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import java.util.Locale;
import javax.servlet.ServletException;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class AutocompleteServletTest {

  private final AemContext context = new AemContextBuilder()
      .plugin(ContextPlugins.CACONFIG)
      .build();

  @Mock
  TypeaheadService typeaheadService;

  @Mock
  I18nProvider i18nProvider;

  @Mock
  PathTransformer pathTransformer;

  @Mock
  I18n i18n;

  AutocompleteServlet testee;

  @BeforeEach
  void setUp() {
    context.registerService(TypeaheadService.class, typeaheadService);
    context.registerService(I18nProvider.class, i18nProvider);
    context.registerService(PathTransformer.class, pathTransformer);
    testee = context.registerInjectActivateService(new AutocompleteServlet());
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource("/content/saas-aem-module/us/en/jcr:content/root/container/container/search");
    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
    context.requestPathInfo().setSelectorString(AutocompleteServlet.AUTOCOMPLETE_SELECTOR);
  }

  @Test
  void testAutocomplete_noSearchTerm() throws ServletException, IOException {
    SlingHttpServletRequest request = context.request();
    SlingHttpServletResponse response = context.response();
    assertThrows(IllegalArgumentException.class, () -> testee.doGet(request, response));
    verify(typeaheadService, never()).getResults(any(SearchCAConfigurationModel.class), anyString(), anyString(),
        any());
  }


  @Test
  void testAutocomplete() throws ServletException, IOException {
    when(i18nProvider.getI18n(any(Locale.class))).thenReturn(i18n);
    context.request().addRequestParameter(SearchTabModel.SEARCH_TERM, "foo");
    MockContextAwareConfig.writeConfiguration(context, context.currentResource().getPath(), SearchConfiguration.class,
        "index", "bar");
    testee.doGet(context.request(), context.response());
    verify(typeaheadService, times(1)).getResults(any(SearchCAConfigurationModel.class), anyString(), anyString(),
        any());
  }
}
