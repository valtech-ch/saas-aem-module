package com.valtech.aem.saas.core.bestbets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.valtech.aem.saas.api.bestbets.BestBetsConsumerService;
import com.valtech.aem.saas.core.http.client.DefaultSearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultBestBetsServiceTest {

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  DefaultBestBetsService testee;

  @BeforeEach
  void setUp(AemContext context) {
    Mockito.when(httpClientBuilderFactory.newBuilder()).thenReturn(HttpClientBuilder.create());
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    context.registerInjectActivateService(new DefaultSearchRequestExecutorService());
    testee = context.registerInjectActivateService(new DefaultBestBetsService());
  }

  @Test
  void testGetBestBetsConsumerService() {
    assertThrows(IllegalArgumentException.class, () -> testee.getBestBetsConsumerService(null));
    assertThrows(IllegalArgumentException.class, () -> testee.getBestBetsConsumerService(""));
    assertThat(testee.getBestBetsConsumerService("foo"), instanceOf(BestBetsConsumerService.class));
  }
}
