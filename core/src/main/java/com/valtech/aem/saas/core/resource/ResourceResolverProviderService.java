package com.valtech.aem.saas.core.resource;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
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

  public static final String SERVICE_USER = "saas-aem-module-service-user";

  @Reference
  private ResourceResolverFactory resourceResolverFactory;

  @Override
  public void resourceResolverConsumer(@NonNull Consumer<ResourceResolver> consumer) {
    resourceResolverFunction(resourceResolver -> {
      consumer.accept(resourceResolver);
      return Void.TYPE;
    });
  }

  @Override
  public <R> Optional<R> resourceResolverFunction(@NonNull Function<ResourceResolver, R> function) {
    return getResourceResolverFunctionForServiceUser(SERVICE_USER, function);
  }

  private <R> Optional<R> getResourceResolverFunctionForServiceUser(@NonNull String subServiceName,
      Function<ResourceResolver, R> function) {
    Map<String, Object> map = Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, subServiceName);
    try (ResourceResolver resolver = resourceResolverFactory.getServiceResourceResolver(map)) {
      return Optional.ofNullable(function.apply(resolver));
    } catch (LoginException e) {
      log.error("Access denied", e);
    }
    return Optional.empty();
  }
}
