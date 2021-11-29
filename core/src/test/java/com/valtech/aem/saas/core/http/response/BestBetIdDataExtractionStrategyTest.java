package com.valtech.aem.saas.core.http.response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.util.Optional;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

class BestBetIdDataExtractionStrategyTest {

  @Test
  void testGetData() {
    BestBetIdDataExtractionStrategy strategy = new BestBetIdDataExtractionStrategy();
    assertThat(strategy.propertyName(), Is.is(BestBetIdDataExtractionStrategy.ID));
    Optional<Integer> bestBetResponse = strategy.getData(new JsonParser().parse(
            new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/addBestBetResponse.json")))
        .getAsJsonObject());
    assertThat(bestBetResponse.isPresent(), is(true));
    assertThat(bestBetResponse.get(), instanceOf(Integer.class));
    assertThat(bestBetResponse.get(), is(23));
  }
}
