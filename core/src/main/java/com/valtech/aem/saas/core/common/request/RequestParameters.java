package com.valtech.aem.saas.core.common.request;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

@RequiredArgsConstructor
public final class RequestParameters {

  @NonNull
  private final SlingHttpServletRequest request;

  public Optional<String> getParameter(String name) {
    return Optional.ofNullable(request.getParameter(name)).filter(StringUtils::isNotBlank);
  }
}
