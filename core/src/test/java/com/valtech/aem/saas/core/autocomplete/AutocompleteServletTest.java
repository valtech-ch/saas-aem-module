package com.valtech.aem.saas.core.autocomplete;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.api.typeahead.dto.TypeaheadPayloadDTO;
import com.valtech.aem.saas.core.fulltextsearch.SearchTabModelImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import java.io.IOException;
import javax.servlet.ServletException;
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

  AutocompleteServlet testee;

  @BeforeEach
  void setUp() {
    context.registerService(TypeaheadService.class, typeaheadService);
    testee = context.registerInjectActivateService(new AutocompleteServlet());
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource("/content/saas-aem-module/us/en/jcr:content");

    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
  }

  @Test
  void testAutocomplete_noSearchTerm() throws ServletException, IOException {
    testee.doGet(context.request(), context.response());
    verify(typeaheadService, never()).getResults(anyString(), any(TypeaheadPayloadDTO.class));
  }

  @Test
  void testAutocomplete_noIndexConfigured() throws ServletException, IOException {
    context.request().addRequestParameter(SearchTabModelImpl.SEARCH_TERM, "foo");
    testee.doGet(context.request(), context.response());
    verify(typeaheadService, never()).getResults(anyString(), any(TypeaheadPayloadDTO.class));
  }

  @Test
  void testAutocomplete() throws ServletException, IOException {
    context.request().addRequestParameter(SearchTabModelImpl.SEARCH_TERM, "foo");
    MockContextAwareConfig.writeConfiguration(context, context.currentResource().getPath(), SearchConfiguration.class,
        "index", "bar");
    testee.doGet(context.request(), context.response());
    verify(typeaheadService, times(1)).getResults(anyString(), any(TypeaheadPayloadDTO.class));
  }
}
