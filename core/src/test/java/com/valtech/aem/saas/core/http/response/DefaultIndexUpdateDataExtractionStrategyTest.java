package com.valtech.aem.saas.core.http.response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.indexing.IndexUpdateResponse;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DefaultIndexUpdateDataExtractionStrategyTest {

  @Test
  void getResponse() {
    DefaultIndexUpdateDataExtractionStrategy strategy = new DefaultIndexUpdateDataExtractionStrategy();
    Assertions.assertThrows(UnsupportedOperationException.class, strategy::propertyName);
    assertThat(strategy.getData(new JsonObject()).isPresent(), is(true));
    assertThat(strategy.getData(new JsonParser().parse(
            new InputStreamReader(getClass().getResourceAsStream("/__files/search/indexupdate/success.json")))
        .getAsJsonObject()).isPresent(), is(true));
    assertThat(strategy.getData(new JsonParser().parse(
            new InputStreamReader(getClass().getResourceAsStream("/__files/search/indexupdate/success.json")))
        .getAsJsonObject()).get(), instanceOf(IndexUpdateResponse.class));
  }
}
