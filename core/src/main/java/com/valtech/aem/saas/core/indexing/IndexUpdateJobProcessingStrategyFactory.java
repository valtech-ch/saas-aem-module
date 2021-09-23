package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class IndexUpdateJobProcessingStrategyFactory {

  private final IndexUpdateService indexUpdateService;

  public IndexUpdateJobProcessingStrategy getStrategy(IndexUpdateAction action) {
    if (IndexUpdateAction.UPDATE == action) {
      return new UpdateStrategy(indexUpdateService);
    }
    if (IndexUpdateAction.DELETE == action) {
      return new DeleteStrategy(indexUpdateService);
    }
    return new FallbackStrategy();
  }

}
