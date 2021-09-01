package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SortQueryTest {

  @Test
  void testQuery() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(null));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(""));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery(null, Sort.DESC));
    Assertions.assertThrows(IllegalArgumentException.class, () -> new SortQuery("", Sort.DESC));
    assertThat(new SortQuery("foo").getEntries().size(), is(1));
    assertThat(new SortQuery("foo", Sort.ASC).getEntries().size(), is(1));
    assertThat(new SortQuery("foo", Sort.DESC).getEntries().size(), is(1));
  }
}
