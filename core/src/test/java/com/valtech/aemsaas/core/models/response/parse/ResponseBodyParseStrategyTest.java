package com.valtech.aemsaas.core.models.response.parse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.valtech.aemsaas.core.models.response.search.ResponseBody;
import java.io.InputStreamReader;
import org.junit.jupiter.api.Test;

class ResponseBodyParseStrategyTest {

  @Test
  void getResponse() {
    ResponseBodyParseStrategy strategy = new ResponseBodyParseStrategy();
    assertThat(strategy.propertyName(), is("response"));
    assertThat(strategy.getResponse(new JsonObject()).isPresent(), is(false));
    assertThat(strategy.getResponse(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
        "/__files/search/fulltext/response.json"))).getAsJsonObject()).isPresent(), is(true));
    assertThat(strategy.getResponse(new JsonParser().parse(new InputStreamReader(getClass().getResourceAsStream(
        "/__files/search/fulltext/response.json"))).getAsJsonObject()).get(), instanceOf(
        ResponseBody.class));
  }
}
