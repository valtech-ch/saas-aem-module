package com.valtech.aem.saas.core.resource;

import java.util.Optional;
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
   * Consumes SaaS service user resource resolver session.
   *
   * @param consumer a lambda function consuming the resource resolver instance.
   */
  void resourceResolverConsumer(@NonNull Consumer<ResourceResolver> consumer);

  /**
   * Consumes SaaS service user resource resolver session and returns a result.
   *
   * @param function a lambda function that consumes the resource resolver instance and returns a certain result.
   * @param <R>      the type of the resulting object.
   * @return the result of the lambda function consuming the resource resolver instance.
   */
  <R> Optional<R> resourceResolverFunction(@NonNull Function<ResourceResolver, R> function);

}
