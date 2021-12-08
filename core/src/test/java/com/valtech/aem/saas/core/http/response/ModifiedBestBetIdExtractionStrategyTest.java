package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class ModifiedBestBetIdExtractionStrategyTest {

    @Test
    void testGetData() {
        ModifiedBestBetIdExtractionStrategy strategy = new ModifiedBestBetIdExtractionStrategy();
        assertThat(strategy.propertyName(), is(ModifiedBestBetIdExtractionStrategy.PN_ID));
        assertThat(strategy.getData(new JsonObject()).isPresent(), is(false));
        assertThat(strategy.getData(new JsonParser().parse(
                                                            new InputStreamReader(getClass().getResourceAsStream(
                                                                    "/__files/search/bestbets/modifiedBestBetResponse.json")))
                                                    .getAsJsonObject()).isPresent(), is(true));
        assertThat(strategy.getData(new JsonParser().parse(
                                                            new InputStreamReader(getClass().getResourceAsStream(
                                                                    "/__files/search/bestbets/modifiedBestBetResponse.json")))
                                                    .getAsJsonObject()).get(), instanceOf(Integer.class));
    }
}
