package com.valtech.aemsaas.core.models.request;

import lombok.RequiredArgsConstructor;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

/**
 * Represents a GET search request. It requires the request uri, including the query string.
 */
@RequiredArgsConstructor
public class SearchRequestGet implements SearchRequest {

  private final String uri;

  @Override
  public HttpUriRequest getRequest() {
    return new HttpGet(uri);
  }
}
