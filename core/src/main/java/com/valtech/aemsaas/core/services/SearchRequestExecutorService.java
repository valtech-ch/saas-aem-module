package com.valtech.aemsaas.core.services;

import com.valtech.aemsaas.core.function.HttpResponseConsumer;
import com.valtech.aemsaas.core.models.request.SearchRequest;
import lombok.NonNull;

/**
 * Provides a method for execution of search requests.
 */
public interface SearchRequestExecutorService {

  /**
   * Executes a prepared search requests.
   *
   * @param searchRequest        http request.
   * @param httpResponseConsumer lambda function containing response consume logic.
   * @param <R>                  resulting object type.
   * @return response consume result.
   */
  <R> R execute(@NonNull SearchRequest searchRequest, @NonNull HttpResponseConsumer<R> httpResponseConsumer);

}
