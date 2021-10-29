package com.valtech.aem.saas.api.bestbets.dto;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class BestBetPayloadDTOTest {

  @Test
  void testValidation() {
    assertThrows(IllegalArgumentException.class, () -> new BestBetPayloadDTO("", "foo", ""));
    assertThrows(IllegalArgumentException.class, () -> new BestBetPayloadDTO("", "foo", ""));
    assertThrows(IllegalArgumentException.class, () -> new BestBetPayloadDTO("", "foo", "baz"));
    assertDoesNotThrow(() -> new BestBetPayloadDTO("qux", "foo", "baz"));
  }
}
