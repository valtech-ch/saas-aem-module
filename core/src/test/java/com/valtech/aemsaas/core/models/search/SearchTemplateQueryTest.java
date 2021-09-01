package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SearchTemplateQueryTest {

  @Test
  void testQuery() {
    assertThrows(IllegalArgumentException.class, () -> new SearchTemplateQuery(null).getEntries());
    assertThat(new SearchTemplateQuery("foo").getEntries().size(), is(1));
  }
}
