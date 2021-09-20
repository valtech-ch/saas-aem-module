package com.valtech.aem.saas.core.fulltextsearch;

import lombok.NoArgsConstructor;

/**
 * A concrete decorator extension of {@link AbstractFulltextSearchConfiguration}. It enables the best bets feature in
 * full text search service.
 */
@NoArgsConstructor
public final class FulltextSearchConfigurationEnableBestBets extends AbstractFulltextSearchConfiguration {

  public FulltextSearchConfigurationEnableBestBets(
      AbstractFulltextSearchConfiguration decorated) {
    super(decorated);
  }

  @Override
  protected IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder applyConfiguration(
      IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder indexFulltextSearchConsumerServiceBuilder) {
    return indexFulltextSearchConsumerServiceBuilder.enableBestBets(true);
  }
}
