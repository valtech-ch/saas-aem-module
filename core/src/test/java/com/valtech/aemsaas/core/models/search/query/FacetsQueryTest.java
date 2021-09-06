package com.valtech.aemsaas.core.models.search.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

import com.valtech.aemsaas.core.models.search.query.FacetsQuery;
import org.junit.jupiter.api.Test;

class FacetsQueryTest {

  @Test
  void testQuery() {
    assertThat(FacetsQuery.builder().build().getEntries(), is(empty()));
    assertThat(FacetsQuery.builder().field(null).build().getEntries(), is(empty()));
    assertThat(FacetsQuery.builder().field("").build().getEntries(), is(empty()));
    assertThat(FacetsQuery.builder().field(null).field("foo").build().getEntries().size(),
        is(1));
    assertThat(FacetsQuery.builder().field("").field("foo").build().getEntries().size(),
        is(1));
    assertThat(FacetsQuery.builder().field("foo").field("bar").build().getEntries().size(),
        is(2));
  }
}
