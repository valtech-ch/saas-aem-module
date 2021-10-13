package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import lombok.RequiredArgsConstructor;

/**
 * Factory for instantiating the appropriate index update job processing strategy, depending on the index update action
 * input.
 */
@RequiredArgsConstructor
public final class IndexUpdateJobProcessingStrategyFactory {

  private final IndexUpdateService indexUpdateService;

  /**
   * Gets the strategy associated to the passed index update action.
   *
   * @param action enum representing the index update action.
   * @return index update strategy
   */
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
