package com.valtech.aem.saas.core.http.response;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.api.bestbets.dto.BestBetDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class BestBetsDataExtractionStrategyTest {

    @Test
    void testGetData() {
        BestBetsDataExtractionStrategy strategy = new BestBetsDataExtractionStrategy();
        Assertions.assertThrows(UnsupportedOperationException.class, strategy::propertyName);
        assertThat(strategy.getData(new JsonArray()).isPresent(), is(true));
        Optional<List<BestBetDTO>> bestBets = strategy.getData(new JsonParser().parse(
                                                                                       new InputStreamReader(getClass().getResourceAsStream("/__files/search/bestbets/getBestBets.json")))
                                                                               .getAsJsonArray());
        assertThat(bestBets.isPresent(), is(true));
        assertThat(bestBets.get(), instanceOf(List.class));
        assertThat(bestBets.get().size(), is(3));
        assertThat(bestBets.get().get(0).getLanguage(), is("en"));
        assertThat(bestBets.get().get(0).getId(), is(1));
        assertThat(bestBets.get().get(0).getIndex(), is("icweb"));
        assertThat(bestBets.get().get(0).getProjectId(), is(1));
        assertThat(bestBets.get().get(0).getTerm(), is("foo"));
        assertThat(bestBets.get().get(0).getUrl(), is("foo/bar"));
    }

}
