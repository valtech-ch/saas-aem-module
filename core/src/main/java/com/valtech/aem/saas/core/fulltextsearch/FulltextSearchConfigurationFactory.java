package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConfiguration;
import lombok.Builder;

@Builder
public final class FulltextSearchConfigurationFactory {

  private final boolean enableAutoSuggest;
  private final boolean enableBestBets;

  public FulltextSearchConfiguration<IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder> getConfiguration() {
    AbstractFulltextSearchConfiguration fulltextSearchConfiguration = null;
    if (enableAutoSuggest) {
      fulltextSearchConfiguration = new FulltextSearchConfigurationEnableAutoSuggest(fulltextSearchConfiguration);
    }
    if (enableBestBets) {
      fulltextSearchConfiguration = new FulltextSearchConfigurationEnableBestBets(fulltextSearchConfiguration);
    }
    return fulltextSearchConfiguration;
  }

}
