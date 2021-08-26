package com.valtech.aemsaas.core.services;

import com.valtech.aemsaas.core.function.HttpResponseConsumer;
import com.valtech.aemsaas.core.models.request.SearchRequest;
import lombok.NonNull;

public interface SearchRequestExecutorService {

  <R> R execute(@NonNull SearchRequest searchRequest, @NonNull HttpResponseConsumer<R> httpResponseConsumer);

}
