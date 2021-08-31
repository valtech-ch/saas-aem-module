package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class SearchTemplateQueryTest {

  @Test
  void getString() {
    assertThat(new SearchTemplateQuery(SearchTemplate.NORMAL).getString(), is(StringUtils.EMPTY));
    assertThat(new SearchTemplateQuery(SearchTemplate.PRODS).getString(), is("tmpl=prods"));
  }
}
