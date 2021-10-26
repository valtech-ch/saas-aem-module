package com.valtech.aem.saas.api.indexing.dto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

class DefaultIndexContentPayloadDTOTest {

  @Test
  void testIndexContentPayloadBuild() {
    DefaultIndexContentPayloadDTO.Builder builder = DefaultIndexContentPayloadDTO.builder();
    assertThrows(IllegalStateException.class, builder::build);
    assertThat(builder
        .content("foo")
        .title("bar")
        .url("baz")
        .repositoryPath("baz")
        .language("de")
        .metaKeywords("foo bar")
        .metaDescription("bar")
        .scope("qux")
        .build(), IsInstanceOf.instanceOf(DefaultIndexContentPayloadDTO.class));
  }
}
