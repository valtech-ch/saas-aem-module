package com.valtech.aem.saas.core.resource;

import java.util.function.Consumer;
import java.util.function.Function;
import lombok.NonNull;
import org.apache.sling.api.resource.ResourceResolver;

/**
 * Represents a service that provides a sub-service based resource resolver instance for consumption, which is safely
 * closed at the end.
 */
public interface ResourceResolverProvider {

  /**
   * Performs instantiation of a resource resolver instance which can be consumed.
   *
   * @param subServiceName the sub service name mapped to a service user.
   * @param consumer       a lambda function consuming the resource resolver instance.
   */
  void resourceResolverConsumer(@NonNull String subServiceName, @NonNull Consumer<ResourceResolver> consumer);

  /**
   * Performs instantiation of a resource resolver instance which can be consumed and return a certain result.
   *
   * @param subServiceName the sub service name mapped to a service user.
   * @param function       a lambda function that consumes the resource resolver instance and returns a certain result.
   * @param <R>            the type of the resulting object.
   * @return the result of the lambda function consuming the resource resolver instance.
   */
  <R> R resourceResolverFunction(@NonNull String subServiceName, @NonNull Function<ResourceResolver, R> function);

}
