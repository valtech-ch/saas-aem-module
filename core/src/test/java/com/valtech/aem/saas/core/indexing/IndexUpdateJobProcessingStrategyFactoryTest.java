package com.valtech.aem.saas.core.indexing;

import static org.hamcrest.core.IsInstanceOf.instanceOf;

import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IndexUpdateJobProcessingStrategyFactoryTest {

  @Mock
  IndexUpdateService indexUpdateService;

  @Test
  void testGetStrategy() {
    IndexUpdateJobProcessingStrategyFactory factory = new IndexUpdateJobProcessingStrategyFactory(indexUpdateService);
    MatcherAssert.assertThat(factory.getStrategy(IndexUpdateAction.UPDATE), instanceOf(UpdateStrategy.class));
    MatcherAssert.assertThat(factory.getStrategy(IndexUpdateAction.DELETE), instanceOf(DeleteStrategy.class));
    MatcherAssert.assertThat(factory.getStrategy(null), instanceOf(FallbackStrategy.class));
  }
}
