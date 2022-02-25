package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFieldResultsDTO;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class FacetFieldsDataExtractionStrategyTest {

    @Test
    void testGetData() {
        FacetFieldsDataExtractionStrategy strategy = new FacetFieldsDataExtractionStrategy();
        assertThat(strategy.getData(new JsonObject()).isPresent(), is(false));
        Optional<List<FacetFieldResultsDTO>> facetFieldResultsDTOList = strategy.getData(
                new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                        "/__files/search/fulltext/response.json"))).getAsJsonObject());
        assertThat(facetFieldResultsDTOList.isPresent(), is(true));
        assertThat(facetFieldResultsDTOList.get().get(0).getFieldName(), is("contentType"));
    }
}
