package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class SearchResultItemTrackingExtractionStrategyTest {

    @Test
    void testGetData() {
        SearchResultItemTrackingExtractionStrategy
                strategy = new SearchResultItemTrackingExtractionStrategy();
        assertThat(strategy.getData(new JsonParser().parse("")).isPresent(), is(false));
        assertThat(strategy.getData(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                "/__files/search/tracking/success.json"))).getAsJsonObject()).isPresent(), is(true));
    }
}
