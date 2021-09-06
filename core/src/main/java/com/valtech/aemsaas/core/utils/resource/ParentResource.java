package com.valtech.aemsaas.core.utils.resource;

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
    return resource.getParent() != null && resource.getParent().isResourceType(resourceType)
        ? resource.getParent()
        : null;
  }
}
