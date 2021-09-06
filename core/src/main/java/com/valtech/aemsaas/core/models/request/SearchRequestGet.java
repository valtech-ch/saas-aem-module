package com.valtech.aemsaas.core.models.request;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

@RequiredArgsConstructor
public class SearchRequestGet implements SearchRequest {

  private final String url;

  @Override
  public @NonNull HttpUriRequest getRequest() {
    return new HttpGet(url);
  }
}
