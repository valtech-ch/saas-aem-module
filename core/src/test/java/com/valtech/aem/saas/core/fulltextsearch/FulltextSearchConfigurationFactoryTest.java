package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConfiguration;
import org.junit.jupiter.api.Test;

class FulltextSearchConfigurationFactoryTest {

  @Test
  void testGetConfiguration() {
    assertThat(FulltextSearchConfigurationFactory.builder().build().getConfiguration(), nullValue());
    assertThat(FulltextSearchConfigurationFactory.builder().enableAutoSuggest(true).build().getConfiguration(),
        instanceOf(FulltextSearchConfigurationEnableAutoSuggest.class));
    assertThat(FulltextSearchConfigurationFactory.builder().enableBestBets(true).build().getConfiguration(),
        instanceOf(FulltextSearchConfigurationEnableBestBets.class));
    assertThat(FulltextSearchConfigurationFactory.builder().enableAutoSuggest(true).enableBestBets(true).build()
        .getConfiguration(), instanceOf(FulltextSearchConfiguration.class));
  }
}
