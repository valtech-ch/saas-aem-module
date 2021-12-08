package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class JsonObjectDataExtractionStrategyTest {

    @Test
    void testGetData() {
        JsonObjectDataExtractionStrategy strategy = new JsonObjectDataExtractionStrategy();
        Assertions.assertThrows(UnsupportedOperationException.class, strategy::propertyName);
        assertThat(strategy.getData(new JsonObject()).isPresent(), is(true));
        Optional<JsonObject> jsonObject = strategy.getData(new JsonParser().parse(
                                                                                   new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/error.json")))
                                                                           .getAsJsonObject());
        assertThat(jsonObject.isPresent(), is(true));
        assertThat(jsonObject.get(), instanceOf(JsonObject.class));
    }
}
