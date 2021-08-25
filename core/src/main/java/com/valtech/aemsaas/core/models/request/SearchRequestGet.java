package com.valtech.aemsaas.core.models.request;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

@RequiredArgsConstructor
public class SearchRequestGet implements SearchRequest {

  private final String url;

  @Override
  public HttpUriRequest getRequest() {
    return new HttpGet(url);
  }
}
