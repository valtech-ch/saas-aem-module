package com.valtech.aem.saas.core.http.response;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aem.saas.core.http.response.dto.ResponseHeaderDto;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Test;

class ResponseHeaderDataExtractionStrategyTest {

  @Test
  void testGetData() {
    ResponseHeaderDataExtractionStrategy strategy = new ResponseHeaderDataExtractionStrategy();
    assertThat(strategy.propertyName(), is("responseHeader"));
    assertThat(strategy.getData(new JsonObject()).isPresent(), is(false));
    assertThat(strategy.getData(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
        "/__files/search/fulltext/response.json"))).getAsJsonObject()).isPresent(), is(true));
    assertThat(strategy.getData(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
        "/__files/search/fulltext/response.json"))).getAsJsonObject()).get(), instanceOf(
        ResponseHeaderDto.class));
  }
}
