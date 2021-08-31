package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class FacetsQueryTest {

  @Test
  void getString() {
    assertThat(FacetsQuery.builder().build().getString(), is(StringUtils.EMPTY));
    assertThat(FacetsQuery.builder().field(null).build().getString(), is(StringUtils.EMPTY));
    assertThat(FacetsQuery.builder().field("").build().getString(), is(StringUtils.EMPTY));
    assertThat(FacetsQuery.builder().field(null).field("foo").build().getString(),
        is(String.format("%s=foo", FacetsQuery.FACETFIELD)));
    assertThat(FacetsQuery.builder().field("").field("foo").build().getString(),
        is(String.format("%s=foo", FacetsQuery.FACETFIELD)));
    assertThat(FacetsQuery.builder().field("foo").field("bar").build().getString(),
        is(String.format("%s=foo&%s=bar", FacetsQuery.FACETFIELD, FacetsQuery.FACETFIELD)));
  }
}
