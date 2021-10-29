package com.valtech.aem.saas.api.indexing.dto;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

class IndexContentPayloadDTOTest {

  @Test
  void testIndexContentPayloadBuild() {
    assertThat(new IndexContentPayloadDTO("foo",
        "bar",
        "baz",
        "baz",
        "de",
        "foo bar",
        "bar",
        "qux"), IsInstanceOf.instanceOf(IndexContentPayloadDTO.class));
  }
}
