package com.valtech.aem.saas.core.util.request;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

@RequiredArgsConstructor
public final class RequestParameters {

  private final SlingHttpServletRequest request;

  public String getParameter(String name) {
    return StringUtils.defaultString(request.getParameter(name));
  }
}
