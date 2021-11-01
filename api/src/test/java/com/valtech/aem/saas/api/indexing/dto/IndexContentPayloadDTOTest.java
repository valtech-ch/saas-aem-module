package com.valtech.aem.saas.api.indexing.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

class IndexContentPayloadDTOTest {

  @Test
  void testIndexContentPayloadBuild() {
    assertThat(new IndexContentPayloadDTO("adventures content that is pushed.",
        "WKND Adventures",
        "https://wknd.site/us/en/adventures.html",
        "/content/wknd/us/en/adventures",
        "en",
        "sailing",
        "boat sailing is great",
        "scope"), IsInstanceOf.instanceOf(IndexContentPayloadDTO.class));

    assertThrows(IllegalArgumentException.class, () -> new IndexContentPayloadDTO("",
        "WKND Adventures",
        "https://wknd.site/us/en/adventures.html",
        "/content/wknd/us/en/adventures",
        "en",
        "sailing",
        "boat sailing is great",
        "scope"));
  }
}
