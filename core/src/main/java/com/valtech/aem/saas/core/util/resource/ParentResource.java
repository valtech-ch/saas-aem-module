package com.valtech.aem.saas.core.util.resource;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;

@RequiredArgsConstructor
public final class ParentResource {

  private final Resource resource;

  public Optional<Resource> getParentWithResourceType(String resourceType) {
    return Optional.ofNullable(getParentWithResourceType(resource, resourceType));
  }

  private Resource getParentWithResourceType(Resource resource, String resourceType) {
    if (resource.getParent() != null) {
      if (resource.getParent().isResourceType(resourceType)) {
        return resource.getParent();
      }
      return getParentWithResourceType(resource.getParent(), resourceType);
    }
    return null;
  }
}
