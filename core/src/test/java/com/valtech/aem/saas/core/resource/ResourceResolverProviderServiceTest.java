package com.valtech.aem.saas.core.resource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.anyMap;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.function.Consumer;
import java.util.function.Function;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ResourceResolverProviderServiceTest {

  @Mock
  ResourceResolverFactory resourceResolverFactory;

  @InjectMocks
  ResourceResolverProvider resourceResolverProvider = new ResourceResolverProviderService();

  @Test
  void resourceResolverConsumer() throws LoginException {
    Consumer<ResourceResolver> consumer = Mockito.mock(Consumer.class);
    resourceResolverProvider.resourceResolverConsumer(consumer);
    verify(resourceResolverFactory, times(1)).getServiceResourceResolver(Mockito.anyMap());
  }

  @Test
  void resourceResolverFunction() throws LoginException {
    Function<ResourceResolver, String> function = Mockito.mock(Function.class);
    resourceResolverProvider.resourceResolverFunction(function);
    verify(resourceResolverFactory, times(1)).getServiceResourceResolver(Mockito.anyMap());
  }

  @Test
  void testResourceResolverLoginException() throws LoginException {
    when(resourceResolverFactory.getServiceResourceResolver(anyMap())).thenThrow(LoginException.class);
    assertThat(resourceResolverProvider.resourceResolverFunction(resourceResolver -> "bar"),
        nullValue());
  }
}
