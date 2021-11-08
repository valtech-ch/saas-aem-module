package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyCollection.emptyCollectionOf;
import static org.hamcrest.core.Is.is;

import com.valtech.aem.saas.api.query.Filter;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;

class ConfiguredFiltersParserTest {

  @Test
  void testGetFilters() {
    assertThat(
        new ConfiguredFiltersParser(Collections.singletonList(new MockFilterModel("foo", "bar"))).getFilters().size(),
        is(1));
    assertThat(
        new ConfiguredFiltersParser(Collections.singletonList(new MockFilterModel(null, "bar"))).getFilters(),
        emptyCollectionOf(Filter.class));
    assertThat(
        new ConfiguredFiltersParser(Collections.singletonList(new MockFilterModel("foo", ""))).getFilters(),
        emptyCollectionOf(Filter.class));
    assertThat(
        new ConfiguredFiltersParser(null).getFilters(),
        emptyCollectionOf(Filter.class));
    assertThat(
        new ConfiguredFiltersParser(
            Arrays.asList(new MockFilterModel("foo", "bar"), new MockFilterModel("baz", "quz,qux"))).getFilters()
            .size(),
        is(2));
  }
}
