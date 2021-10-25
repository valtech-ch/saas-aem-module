package com.valtech.aem.saas.api.bestbets.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class DefaultBestBetPayloadDTOTest {

  @Test
  void testValidation() {
    assertThrows(IllegalArgumentException.class, () -> new DefaultBestBetPayloadDTO("", "foo", "", ""));
    assertThrows(IllegalArgumentException.class, () -> new DefaultBestBetPayloadDTO("", "foo", "bar", ""));
    assertThrows(IllegalArgumentException.class, () -> new DefaultBestBetPayloadDTO("", "foo", "bar", "baz"));
    assertDoesNotThrow(() -> new DefaultBestBetPayloadDTO("qux", "foo", "bar", "baz"));
  }
}
