package com.valtech.aem.saas.core.fulltextsearch;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FulltextSearchConfigurationEnableAutoSuggestTest {

  @Test
  void testApply() {
    IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder builder = spy(
        IndexFulltextSearchConsumerService.builder());
    new FulltextSearchConfigurationEnableAutoSuggest().apply(builder);
    verify(builder, times(1)).enableAutoSuggest(true);
  }
}
