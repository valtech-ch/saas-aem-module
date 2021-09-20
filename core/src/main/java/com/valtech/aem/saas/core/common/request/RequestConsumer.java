package com.valtech.aem.saas.core.common.request;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.valtech.aem.saas.core.util.LoggedOptional;
import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * A helper class for consuming a request.
 */
@RequiredArgsConstructor
public final class RequestConsumer {

  @NonNull
  private final SlingHttpServletRequest request;

  /**
   * Gets a parameter by name.
   *
   * @param name parameter name
   * @return empty optional if parameter with specified name is not existing or the parameter's value is blank.
   */
  public Optional<String> getParameter(String name) {
    return Optional.ofNullable(request.getParameter(name)).filter(StringUtils::isNotBlank);
  }

  /**
   * Gets the request's suffix
   *
   * @return suffix string or optional empty if suffix is not found.
   */
  public Optional<String> getSuffix() {
    return Optional.ofNullable(request.getRequestPathInfo().getSuffix()).filter(StringUtils::isNotBlank);
  }

  public Optional<Page> getCurrentPage() {
    return LoggedOptional.of(request.getResourceResolver().adaptTo(PageManager.class),
            logger -> logger.error("Can not obtain page manager object."))
        .flatMap(pageManager -> LoggedOptional.of(pageManager.getContainingPage(request.getResource()),
            logger -> logger.warn("Containing page, for resource [{}], not found", request.getResource().getPath())));

  }
}
