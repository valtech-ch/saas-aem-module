package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConsumerService;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
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
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultFulltextSearchServiceTest {

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  FulltextSearchService service;
  FulltextSearchConfigurationService configService;

  @BeforeEach
  void setUp(AemContext context) {
    when(httpClientBuilderFactory.newBuilder()).thenReturn(HttpClientBuilder.create());
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    context.registerInjectActivateService(new DefaultSearchRequestExecutorService());
    service = context.registerInjectActivateService(new DefaultFulltextSearchService());
    configService = context.registerInjectActivateService(new DefaultFulltextSearchService());
  }

  @Test
  void getTypeaheadConsumerService() {
    assertThrows(IllegalArgumentException.class, () -> service.getFulltextSearchConsumerService(null, null));
    assertThrows(IllegalArgumentException.class, () -> service.getFulltextSearchConsumerService("", null));
    assertThat(service.getFulltextSearchConsumerService("foo", null), instanceOf(FulltextSearchConsumerService.class));
  }

  @Test
  void getAllowedFilterFields() {
    assertThat(configService.getRowsMaxLimit(), is(9999));
  }

}
