package com.valtech.aem.saas.core.bestbets;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DefaultBestBetPayloadTest {

  @Test
  void testValidation() {
    assertThrows(IllegalArgumentException.class, () -> new DefaultBestBetPayload("", "foo", "", ""));
    assertThrows(IllegalArgumentException.class, () -> new DefaultBestBetPayload("", "foo", "bar", ""));
    assertThrows(IllegalArgumentException.class, () -> new DefaultBestBetPayload("", "foo", "bar", "baz"));
    assertDoesNotThrow(() -> new DefaultBestBetPayload("qux", "foo", "bar", "baz"));
  }
}
