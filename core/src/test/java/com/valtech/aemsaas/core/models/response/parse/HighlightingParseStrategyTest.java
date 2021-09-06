package com.valtech.aemsaas.core.models.response.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aemsaas.core.models.response.search.Highlighting;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class HighlightingParseStrategyTest {

  @Test
  void getResponse() {
    HighlightingDataExtractionStrategy strategy = new HighlightingDataExtractionStrategy();
    Assertions.assertThrows(UnsupportedOperationException.class, strategy::propertyName);
    assertThat(strategy.getData(new JsonObject()).isPresent(), is(true));
    assertThat(strategy.getData(new JsonParser().parse(
            new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
        .getAsJsonObject()).isPresent(), is(true));
    assertThat(strategy.getData(new JsonParser().parse(
            new InputStreamReader(getClass().getResourceAsStream("/__files/search/fulltext/response.json")))
        .getAsJsonObject()).get(), instanceOf(Highlighting.class));
  }
}
