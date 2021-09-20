package com.valtech.aem.saas.core.fulltextsearch;

import lombok.NoArgsConstructor;

/**
 * A concrete decorator extension of {@link AbstractFulltextSearchConfiguration}. It enables the auto suggest feature in
 * full text search service.
 */
@NoArgsConstructor
public final class FulltextSearchConfigurationEnableAutoSuggest extends AbstractFulltextSearchConfiguration {

  public FulltextSearchConfigurationEnableAutoSuggest(
      AbstractFulltextSearchConfiguration decorated) {
    super(decorated);
  }

  @Override
  protected IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder applyConfiguration(
      IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder indexFulltextSearchConsumerServiceBuilder) {
    return indexFulltextSearchConsumerServiceBuilder.enableAutoSuggest(true);
  }
}
