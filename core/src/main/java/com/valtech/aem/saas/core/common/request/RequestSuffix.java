package com.valtech.aem.saas.core.common.request;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

@RequiredArgsConstructor
public final class RequestSuffix {

  @NonNull
  private final SlingHttpServletRequest request;

  public Optional<String> getSuffix() {
    return Optional.ofNullable(request.getRequestPathInfo().getSuffix()).filter(StringUtils::isNotBlank);
  }
}
