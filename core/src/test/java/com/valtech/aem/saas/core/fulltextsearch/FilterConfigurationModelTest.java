package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.query.Filter;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;

@ExtendWith({AemContextExtension.class})
class FilterConfigurationModelTest {

    private final AemContext context = new AemContext();

    FilterConfigurationModel testee;

    @BeforeEach
    void setUp() {
        context.load()
               .json("/content/components/searchtab/filters/content.json",
                     "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab" +
                             "/filters");
    }

    @Test
    void testSingleValueFilter() {
        testFilterValue(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item1",
                "domain:https://wknd.site");
    }

    @Test
    void testSingleValueWithWhitespaceFilter() {
        testFilterValue(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item4",
                "keyword_mstr:\"Hunting & Fishing\"");
    }

    @Test
    void testMultiValueFilter() {
        testFilterValue("/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab" +
                                "/filters/item0",
                        "(contentType:pdf OR contentType:xls)");
    }

    @Test
    void testInvalidFilter_missingFilterName() {
        context.currentResource(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item2");
        adaptResource(context);
        testAdaptable();
        assertThat(testee.isValid(), is(false));
    }

    @Test
    void testInvalidFilter_missingFilterValue() {
        context.currentResource(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item3");
        adaptResource(context);
        testAdaptable();
        assertThat(testee.isValid(), is(false));
    }

    private void adaptResource(AemContext context) {
        testee = context.currentResource().adaptTo(FilterConfigurationModel.class);
    }


    private void testAdaptable() {
        assertThat(testee, notNullValue());
        assertThat(testee, instanceOf(FilterConfiguration.class));
    }

    private void testFilterValue(String filterEntryResourcePath, String expected) {
        context.currentResource(filterEntryResourcePath);
        adaptResource(context);
        testAdaptable();
        assertThat(testee.getFilter(), instanceOf(Filter.class));
        assertThat(testee.getFilter().getQueryString(), is(expected));
    }

}
