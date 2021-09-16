package com.valtech.aem.saas.core.http.response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.bestbets.BestBet;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BestBetsDataExtractionStrategyTest {

  @Test
  void testGetData() {
    BestBetsDataExtractionStrategy strategy = new BestBetsDataExtractionStrategy();
    Assertions.assertThrows(UnsupportedOperationException.class, strategy::propertyName);
    assertThat(strategy.getData(new JsonArray()).isPresent(), is(true));
    Optional<List<BestBet>> bestBets = strategy.getData(new JsonParser().parse(
            new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/getBestBets.json")))
        .getAsJsonArray());
    assertThat(bestBets.isPresent(), is(true));
    assertThat(bestBets.get(), instanceOf(List.class));
    assertThat(bestBets.get().size(), is(3));
  }

}
