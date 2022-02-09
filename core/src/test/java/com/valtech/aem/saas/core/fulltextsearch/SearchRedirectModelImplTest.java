package com.valtech.aem.saas.core.fulltextsearch;

import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.SearchRedirectModel;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import com.valtech.aem.saas.core.resource.MockPathTransformer;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class SearchRedirectModelImplTest {

    private final AemContext context = new AemContextBuilder()
            .plugin(ContextPlugins.CACONFIG)
            .build();
    @Mock
    I18nProvider i18nProvider;

    @Mock
    I18n i18n;

    SearchRedirectModel testee;

    @BeforeEach
    void setUp() {
        context.registerService(I18nProvider.class, i18nProvider);

        context.registerService(PathTransformer.class, new MockPathTransformer());
        context.load().json("/content/searchpage/content.json", "/content/saas-aem-module/us/en");
        context.load().json("/content/searchredirect/content.json", "/content/saas-aem-module/us/en/searchredirect");
        MockContextAwareConfig.registerAnnotationClasses(context, SearchConfiguration.class);
    }

    @Test
    void testSearchRedirectModel() {
        when(i18nProvider.getI18n(any())).thenReturn(i18n);
        context.currentResource(
                "/content/saas-aem-module/us/en/searchredirect/jcr:content/root/container/searchredirect");
        testAdaptable();
        assertThat(testee.getAutocompleteTriggerThreshold(),
                   is(SearchConfiguration.DEFAULT_AUTOCOMPLETE_TRIGGER_THRESHOLD));
        assertThat(testee.getAutocompleteUrl(),
                   is("/content/saas-aem-module/us/en/jcr:content/root/container/container/search.autocomplete.json"));
        assertThat(testee.getSearchUrl(), is("/content/saas-aem-module/us/en.html"));
        assertThat(testee.getSearchFieldPlaceholderText(), is("Type search term here...(overridden)"));
        assertThat(testee.getId(), not(isEmptyString()));
    }

    @Test
    void testPlaceholderTextOverride() {
        when(i18nProvider.getI18n(any())).thenReturn(i18n);
        context.currentResource(
                "/content/saas-aem-module/us/en/searchredirect/jcr:content/root/container/searchredirect_fallback");
        testAdaptable();
        assertThat(testee.getSearchFieldPlaceholderText(), is("Type search term here..."));
    }

    @Test
    void testSearchRedirectModel_noSearchPagePath() {
        context.currentResource(
                "/content/saas-aem-module/us/en/searchredirect/jcr:content/root/container/searchredirect_noSearchPagePath");
        testAdaptable();
        assertThat(testee.getAutocompleteUrl(), is(nullValue()));
        assertThat(testee.getSearchUrl(), is(nullValue()));
        assertThat(testee.getSearchFieldPlaceholderText(), is(nullValue()));
    }

    @Test
    void testSearchRedirectModel_noExistingSearchPagePath() {
        context.currentResource(
                "/content/saas-aem-module/us/en/searchredirect/jcr:content/root/container/searchredirect_nonExistingSearchPagePath");
        testAdaptable();
        assertThat(testee.getAutocompleteUrl(), is(nullValue()));
        assertThat(testee.getSearchUrl(), is(nullValue()));
        assertThat(testee.getSearchFieldPlaceholderText(), is(nullValue()));
    }

    private void testAdaptable() {
        testee = context.request().adaptTo(SearchRedirectModel.class);
        assertThat(testee, notNullValue());
        assertThat(testee, instanceOf(SearchRedirectModel.class));
    }
}
