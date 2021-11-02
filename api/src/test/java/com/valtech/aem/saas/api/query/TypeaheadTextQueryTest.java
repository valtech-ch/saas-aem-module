package com.valtech.aem.saas.api.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.core.Is.is;

import org.junit.jupiter.api.Test;

class TypeaheadTextQueryTest {

  @Test
  void testQuery() {
    assertThat(new TypeaheadTextQuery(null).getEntries(), is(empty()));
    assertThat(new TypeaheadTextQuery("").getEntries(), is(empty()));
    assertThat(new TypeaheadTextQuery("foo  ").getEntries().size(), is(2));
    assertThat(new TypeaheadTextQuery(" foo bar").getEntries().size(), is(2));
    TypeaheadTextQuery typeaheadTextQuery = new TypeaheadTextQuery("foo  bar ");
    assertThat(typeaheadTextQuery.getEntries().size(), is(2));
    assertThat(typeaheadTextQuery.getEntries().get(0).getValue(), endsWith("*"));
  }
}
