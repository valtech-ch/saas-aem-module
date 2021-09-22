package com.valtech.aem.saas.core.resource;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Slf4j
@Component(name = "Search as a Service - ResourceResolver Provider Service",
    service = ResourceResolverProvider.class)
public class ResourceResolverProviderService implements ResourceResolverProvider {

  @Reference
  private ResourceResolverFactory resourceResolverFactory;

  @Override
  public void resourceResolverConsumer(@NonNull String subServiceName, @NonNull Consumer<ResourceResolver> consumer) {
    resourceResolverFunction(subServiceName, (ResourceResolver resourceResolver) -> {
      consumer.accept(resourceResolver);
      return Void.TYPE;
    });
  }

  @Override
  public <R> R resourceResolverFunction(@NonNull String subServiceName,
      @NonNull Function<ResourceResolver, R> function) {
    return getResourceResolverFunctionForServiceUser(subServiceName, function);
  }

  private <R> R getResourceResolverFunctionForServiceUser(@NonNull String subServiceName,
      Function<ResourceResolver, R> function) {
    Map<String, Object> map = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, subServiceName);
    try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(map)) {
      return function.apply(resolver);
    } catch (LoginException e) {
      log.error("Access denied", e);
      return null;
    }
  }
}
