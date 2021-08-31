package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class HighlightingDisableQueryTest {

  @Test
  void getString() {
    assertThat(new HighlightingDisableQuery().getString(),
        is(String.format("%s=false", HighlightingDisableQuery.HIGHLIGHT_SWITCH)));
  }
}
