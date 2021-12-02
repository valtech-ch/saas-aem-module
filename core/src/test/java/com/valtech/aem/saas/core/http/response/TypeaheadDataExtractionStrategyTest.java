package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.core.http.response.dto.FacetCountsDTO;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class TypeaheadDataExtractionStrategyTest {

    @Test
    void testGetData() {
        TypeaheadDataExtractionStrategy strategy = new TypeaheadDataExtractionStrategy("de");
        assertThat(strategy.propertyName(), is(FacetCountsDTO.PN_FACET_COUNTS));
        assertThat(strategy.getData(new JsonObject()).isPresent(), is(false));
        JsonObject response = new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                "/__files/search/typeahead/success.json"))).getAsJsonObject();
        assertThat(strategy.getData(response).isPresent(), is(true));
        assertThat(strategy.getData(response).get(), instanceOf(
                List.class));

    }

    @Test
    void testGetData_noLanguageStrategy() {
        TypeaheadDataExtractionStrategy nullLanguageStrategy = new TypeaheadDataExtractionStrategy(null);
        TypeaheadDataExtractionStrategy emptyLanguageStrategy = new TypeaheadDataExtractionStrategy("");
        TypeaheadDataExtractionStrategy falseLanguageStrategy = new TypeaheadDataExtractionStrategy("foo");

        JsonObject response = new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                "/__files/search/typeahead/success.json"))).getAsJsonObject();

        assertThat(nullLanguageStrategy.getData(response).get(), is(empty()));
        assertThat(emptyLanguageStrategy.getData(response).get(), is(empty()));
        assertThat(falseLanguageStrategy.getData(response).get(), is(empty()));
    }

}
