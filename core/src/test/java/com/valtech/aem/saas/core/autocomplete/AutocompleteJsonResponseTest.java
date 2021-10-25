package com.valtech.aem.saas.core.autocomplete;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.typeahead.dto.TypeaheadPayloadDTO;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.fulltextsearch.SearchTabModelImpl;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextBuilder;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.testing.mock.caconfig.ContextPlugins;
import org.apache.sling.testing.mock.caconfig.MockContextAwareConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class AutocompleteJsonResponseTest {

  private final AemContext context = new AemContextBuilder()
      .plugin(ContextPlugins.CACONFIG)
      .build();

  @Mock
  TypeaheadService typeaheadService;

  AutocompleteJsonResponse testee;

  @BeforeEach
  void setUp() {
    context.registerService(TypeaheadService.class, typeaheadService);
    context.create().resource("/content/saas-aem-module", "sling:configRef", "/conf/saas-aem-module");
    context.create().page("/content/saas-aem-module/us");
    context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
    context.currentPage("/content/saas-aem-module/us/en");
    context.currentResource("/content/saas-aem-module/us/en/jcr:content");

    MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
  }

  @Test
  void testResponseAlreadyCommitted() {
    context.response().flushBuffer();
    SlingHttpServletRequest requestSpy = spy(context.request());
    adaptRequest(context.request());
    testAdaptable();
    assertThat(testee, notNullValue());
    verify(requestSpy, never()).adaptTo(RequestWrapper.class);
  }

  @Test
  void testAutocomplete_noSearchTerm() {
    adaptRequest(context.request());
    testAdaptable();
    assertThat(testee, notNullValue());
    verify(typeaheadService, never()).getResults(anyString(), any(TypeaheadPayloadDTO.class));
  }

  @Test
  void testAutocomplete_noIndexConfigured() {
    context.request().addRequestParameter(SearchTabModelImpl.SEARCH_TERM, "foo");
    adaptRequest(context.request());
    testAdaptable();
    verify(typeaheadService, never()).getResults(anyString(), any(TypeaheadPayloadDTO.class));
  }

  @Test
  void testAutocomplete() {
    context.request().addRequestParameter(SearchTabModelImpl.SEARCH_TERM, "foo");
    MockContextAwareConfig.writeConfiguration(context, context.currentResource().getPath(), SearchConfiguration.class,
        "index", "bar");
    adaptRequest(context.request());
    testAdaptable();
    verify(typeaheadService, times(1)).getResults(anyString(), any(TypeaheadPayloadDTO.class));
  }

  private void testAdaptable() {
    assertThat(testee, notNullValue());
    assertThat(testee, instanceOf(AutocompleteJsonResponse.class));
  }

  private void adaptRequest(SlingHttpServletRequest request) {
    testee = request.adaptTo(AutocompleteJsonResponse.class);
  }
}
