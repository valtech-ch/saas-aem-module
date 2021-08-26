package com.valtech.aemsaas.core.function;

import java.util.function.Function;
import org.apache.http.client.methods.CloseableHttpResponse;

/**
 * Represents a function that accepts an argument of type CloseableHttpResponse and produces a result.
 *
 * @param <R> result type
 */
@FunctionalInterface
public interface HttpResponseConsumer<R> extends Function<CloseableHttpResponse, R> {

}
