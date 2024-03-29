package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.core.http.response.dto.ResponseBodyDTO;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class ResponseBodyDataExtractionStrategyTest {

    @Test
    void testGetData() {
        ResponseBodyDataExtractionStrategy strategy = new ResponseBodyDataExtractionStrategy();
        assertThat(strategy.propertyName(), is("response"));
        assertThat(strategy.getData(new JsonObject()).isPresent(), is(false));
        assertThat(strategy.getData(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                "/__files/search/fulltext/response.json"))).getAsJsonObject()).isPresent(), is(true));
        assertThat(strategy.getData(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
                "/__files/search/fulltext/response.json"))).getAsJsonObject()).get(), instanceOf(
                ResponseBodyDTO.class));
    }
}
