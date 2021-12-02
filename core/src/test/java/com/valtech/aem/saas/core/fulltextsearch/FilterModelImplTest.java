package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
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
import static org.hamcrest.core.IsNull.nullValue;

@ExtendWith({AemContextExtension.class})
class FilterModelImplTest {

    FilterModel testee;

    @BeforeEach
    void setUp(AemContext context) {
        context.load().json("/content/components/searchtab/filters/content.json",
                            "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters");
    }

    @Test
    void testSingleValueFilter(AemContext context) {
        context.currentResource(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item1");
        adaptResource(context);
        testAdaptable();
        assertThat(testee.getFilter(), instanceOf(Filter.class));
        assertThat(testee.getFilter().getQueryString(), is("domain:https://wknd.site"));
    }

    @Test
    void testMultiValueFilter(AemContext context) {
        context.currentResource(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item0");
        adaptResource(context);
        testAdaptable();
        assertThat(testee.getFilter(), instanceOf(Filter.class));
        assertThat(testee.getFilter().getQueryString(), is("(contentType:pdf OR contentType:xls)"));
    }

    @Test
    void testInvalidFilter_missingFilterName(AemContext context) {
        context.currentResource(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item2");
        adaptResource(context);
        testAdaptable();
        assertThat(testee.getFilter(), nullValue());
    }

    @Test
    void testInvalidFilter_missingFilterValue(AemContext context) {
        context.currentResource(
                "/content/saas-aem-module/us/en/jcr:content/root/container/search/search-tabs/searchtab/filters/item3");
        adaptResource(context);
        testAdaptable();
        assertThat(testee.getFilter(), nullValue());
    }

    private void adaptResource(AemContext context) {
        testee = context.currentResource().adaptTo(FilterModel.class);
    }


    private void testAdaptable() {
        assertThat(testee, notNullValue());
        assertThat(testee, instanceOf(FilterModel.class));
    }

}
