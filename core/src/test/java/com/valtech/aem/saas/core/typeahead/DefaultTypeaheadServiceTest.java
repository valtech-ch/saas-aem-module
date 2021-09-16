package com.valtech.aem.saas.core.typeahead;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.valtech.aem.saas.api.typeahead.TypeaheadConfigurationService;
import com.valtech.aem.saas.api.typeahead.TypeaheadConsumerService;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.http.client.DefaultSearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.DefaultSearchServiceConnectionConfigurationService;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({AemContextExtension.class, MockitoExtension.class})
class DefaultTypeaheadServiceTest {

  @Mock
  HttpClientBuilderFactory httpClientBuilderFactory;

  TypeaheadService service;
  TypeaheadConfigurationService configService;

  @BeforeEach
  void setUp(AemContext context) {
    Mockito.when(httpClientBuilderFactory.newBuilder()).thenReturn(HttpClientBuilder.create());
    context.registerService(HttpClientBuilderFactory.class, httpClientBuilderFactory);
    context.registerInjectActivateService(new DefaultSearchServiceConnectionConfigurationService());
    context.registerInjectActivateService(new DefaultSearchRequestExecutorService());
    service = context.registerInjectActivateService(new DefaultTypeaheadService());
    configService = context.registerInjectActivateService(new DefaultTypeaheadService());
  }

  @Test
  void getTypeaheadConsumerService() {
    assertThrows(IllegalArgumentException.class, () -> service.getTypeaheadConsumerService(null));
    assertThrows(IllegalArgumentException.class, () -> service.getTypeaheadConsumerService(""));
    assertThat(service.getTypeaheadConsumerService("foo"), instanceOf(TypeaheadConsumerService.class));
  }

  @Test
  void getAllowedFilterFields() {
    assertThat(configService.getAllowedFilterFields(), Is.is(not(empty())));
  }
}
