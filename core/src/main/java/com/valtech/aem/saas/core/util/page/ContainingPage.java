package com.valtech.aem.saas.core.util.page;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.valtech.aem.saas.core.util.LoggedOptional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

@Slf4j
@RequiredArgsConstructor
public final class ContainingPage {

  private final ResourceResolver resourceResolver;

  public Optional<Page> get(Resource resource) {
    return getPageManager().flatMap(pageManager -> LoggedOptional.of(pageManager.getContainingPage(resource),
        logger -> logger.warn("Containing page, for resource [{}], not found", resource.getPath())));
  }

  private Optional<PageManager> getPageManager() {
    return LoggedOptional.of(resourceResolver.adaptTo(PageManager.class),
        logger -> logger.error("Can not obrain page manager object."));
  }
}
