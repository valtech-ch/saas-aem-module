package com.valtech.aem.saas.core.resource;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.NonNull;
import org.apache.sling.api.resource.ResourceResolver;

public interface ResourceResolverProvider {

  void resourceResolverConsumer(@NonNull String subServiceName, @NonNull Consumer<ResourceResolver> consumer);

  <R> R resourceResolverFunction(@NonNull String subServiceName, @NonNull Function<ResourceResolver, R> function);

}
