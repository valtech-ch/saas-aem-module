package com.valtech.aemsaas.core.utils.request;

import lombok.RequiredArgsConstructor;
import org.apache.sling.api.SlingHttpServletRequest;

@RequiredArgsConstructor
public final class RequestSuffix {

  private final SlingHttpServletRequest request;

  public String getSuffix() {
    return request.getRequestPathInfo().getSuffix();
  }
}
