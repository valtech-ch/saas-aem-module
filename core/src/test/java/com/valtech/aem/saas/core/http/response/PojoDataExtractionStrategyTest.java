package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.tracking.dto.UrlTrackingDTO;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PojoDataExtractionStrategyTest {

    @Test
    void testGetData() {
        PojoDataExtractionStrategy strategy = new PojoDataExtractionStrategy(UrlTrackingDTO.class);
        assertThat(strategy.getData(new JsonParser().parse("")).isPresent(), is(false));
        assertThat(strategy.getData(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                "/__files/search/tracking/success.json"))).getAsJsonObject()).isPresent(), is(true));
    }
}
