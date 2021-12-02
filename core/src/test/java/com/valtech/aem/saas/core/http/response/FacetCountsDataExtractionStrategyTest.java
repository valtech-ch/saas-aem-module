package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.core.http.response.dto.FacetCountsDTO;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FacetCountsDataExtractionStrategyTest {

    @Test
    void testGetData() {
        FacetCountsDataExtractionStrategy strategy = new FacetCountsDataExtractionStrategy();
        assertThat(strategy.propertyName(), is("facet_counts"));
        assertThat(strategy.getData(new JsonObject()).isPresent(), is(false));
        Optional<FacetCountsDTO> facetCountsDTOOptional = strategy.getData(
                new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                        "/__files/search/fulltext/response.json"))).getAsJsonObject());
        assertThat(facetCountsDTOOptional.isPresent(), is(true));
        assertThat(facetCountsDTOOptional.get().getFacetFields().isEmpty(), is(false));
    }

}
