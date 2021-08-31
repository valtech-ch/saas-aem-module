package com.valtech.aemsaas.core.models.search;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

class LanguageQueryTest {

  @Test
  void getString() {
    assertThat(new LanguageQuery(null).getString(), is(StringUtils.EMPTY));
    assertThat(new LanguageQuery("").getString(), is(StringUtils.EMPTY));
    assertThat(new LanguageQuery("de").getString(), is(String.format("%s=de", LanguageQuery.PARAMETER)));
  }
}
