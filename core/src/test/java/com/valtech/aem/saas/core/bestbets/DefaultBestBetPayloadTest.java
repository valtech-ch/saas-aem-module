package com.valtech.aem.saas.core.bestbets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

class DefaultBestBetPayloadTest {

  @Test
  void testBuilder() {
    testIncompletePayload(DefaultBestBetPayload.builder()
        .url("foo"));
    testIncompletePayload(DefaultBestBetPayload.builder()
        .url("foo")
        .term("bar")
        .language("baz"));
    testIncompletePayload(DefaultBestBetPayload.builder()
        .url("foo")
        .term("bar"));
    DefaultBestBetPayload payload = DefaultBestBetPayload.builder()
        .url("foo")
        .term("bar")
        .language("baz")
        .index("qux")
        .build();
    assertThat(payload, Is.is(notNullValue()));
  }

  private void testIncompletePayload(DefaultBestBetPayload.Builder builder) {
    assertThrows(IllegalStateException.class, builder::build);
  }
}
