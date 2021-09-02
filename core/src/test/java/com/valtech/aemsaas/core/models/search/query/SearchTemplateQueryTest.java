package com.valtech.aemsaas.core.models.search.query;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

import com.valtech.aemsaas.core.models.search.query.SearchTemplateQuery;
import org.junit.jupiter.api.Test;

class SearchTemplateQueryTest {

  @Test
  void testQuery() {
    assertThrows(IllegalArgumentException.class, () -> new SearchTemplateQuery(null).getEntries());
    assertThat(new SearchTemplateQuery("foo").getEntries().size(), is(1));
  }
}
