package com.valtech.aem.saas.core.indexing;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.Test;

class DefaultIndexContentPayloadTest {

  @Test
  void testIndexContentPayloadBuild() {
    DefaultIndexContentPayload.Builder builder = DefaultIndexContentPayload.builder();
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
        .build(), IsInstanceOf.instanceOf(DefaultIndexContentPayload.class));
  }
}
