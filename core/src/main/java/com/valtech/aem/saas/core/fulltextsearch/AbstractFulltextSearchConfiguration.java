package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConfiguration;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Abstract implementation of {@link FulltextSearchConfiguration}. It utilizes the decorator pattern for extendable
 * fulltext search configuration implementation.
 */
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractFulltextSearchConfiguration implements
    FulltextSearchConfiguration<IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder> {

  @Getter(AccessLevel.PROTECTED)
  private AbstractFulltextSearchConfiguration decorated;

  @Override
  public IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder apply(
      IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder t) {
    if (decorated != null) {
      return applyConfiguration(decorated.apply(t));
    } else {
      return applyConfiguration(t);
    }
  }

  protected abstract IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder applyConfiguration(
      IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder indexFulltextSearchConsumerServiceBuilder);
}
