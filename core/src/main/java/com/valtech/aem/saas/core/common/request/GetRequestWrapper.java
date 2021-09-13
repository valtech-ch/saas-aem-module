package com.valtech.aem.saas.core.common.request;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.wrappers.SlingHttpServletRequestWrapper;

@RequiredArgsConstructor
public final class GetRequestWrapper {

  @NonNull
  private final SlingHttpServletRequest request;

  public SlingHttpServletRequest getRequest() {
    return new SlingHttpServletRequestWrapper(request) {
      @Override
      public String getMethod() {
        return HttpConstants.METHOD_GET;
      }
    };
  }

}
