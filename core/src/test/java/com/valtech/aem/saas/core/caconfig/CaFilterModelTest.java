package com.valtech.aem.saas.core.caconfig;

import com.valtech.aem.saas.api.caconfig.SearchFilterConfiguration;
import com.valtech.aem.saas.api.query.CompositeFilter;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.SimpleFilter;
import org.hamcrest.core.Is;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CaFilterModelTest {

    @Test
    void testGetFilter_simple() {
        SearchFilterConfiguration searchFilterConfiguration = mock(SearchFilterConfiguration.class);
        when(searchFilterConfiguration.name()).thenReturn("foo");
        when(searchFilterConfiguration.value()).thenReturn("bar");
        CaFilterConfigurationModel testee = new CaFilterConfigurationModel(searchFilterConfiguration);
        Filter simpleFilter = testee.getFilter();
        assertThat(simpleFilter, IsInstanceOf.instanceOf(SimpleFilter.class));
    }

    @Test
    void testGetFilter_multiValue() {
        SearchFilterConfiguration searchFilterConfiguration = mock(SearchFilterConfiguration.class);
        when(searchFilterConfiguration.name()).thenReturn("foo");
        when(searchFilterConfiguration.value()).thenReturn("bar,qux");
        CaFilterConfigurationModel testee = new CaFilterConfigurationModel(searchFilterConfiguration);
        Filter multiValueFilter = testee.getFilter();
        assertThat(multiValueFilter, IsInstanceOf.instanceOf(CompositeFilter.class));
    }

    @Test
    void testIsValid() {
        SearchFilterConfiguration searchFilterConfigurationValid = mock(SearchFilterConfiguration.class);
        when(searchFilterConfigurationValid.name()).thenReturn("foo");
        when(searchFilterConfigurationValid.value()).thenReturn("bar");
        CaFilterConfigurationModel testee = new CaFilterConfigurationModel(searchFilterConfigurationValid);
        assertThat(testee.isValid(), Is.is(true));
    }

    @Test
    void testIsValid_invalid() {
        SearchFilterConfiguration searchFilterConfigurationInvalid = mock(SearchFilterConfiguration.class);
        when(searchFilterConfigurationInvalid.name()).thenReturn("foo");
        when(searchFilterConfigurationInvalid.value()).thenReturn("");
        CaFilterConfigurationModel testee = new CaFilterConfigurationModel(searchFilterConfigurationInvalid);
        assertThat(testee.isValid(), Is.is(false));

    }
}
