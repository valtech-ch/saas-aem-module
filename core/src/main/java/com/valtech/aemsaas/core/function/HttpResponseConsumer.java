package com.valtech.aemsaas.core.function;

import java.util.function.Function;
import org.apache.http.client.methods.CloseableHttpResponse;

@FunctionalInterface
public interface HttpResponseConsumer<R> extends Function<CloseableHttpResponse, R> {

}
