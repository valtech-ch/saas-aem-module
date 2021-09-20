package com.valtech.aem.saas.core.common.resource;

import java.util.Optional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;

@RequiredArgsConstructor
public final class ParentResource {

  @NonNull
  private final Resource resource;

  public Optional<Resource> getParentWithResourceType(String resourceType) {
    return Optional.ofNullable(getParentWithResourceType(resource, resourceType));
  }

  private Resource getParentWithResourceType(@NonNull Resource resource, String resourceType) {
    if (resource.getParent() != null) {
      if (resource.getParent().isResourceType(resourceType)) {
        return resource.getParent();
      }
      return getParentWithResourceType(resource.getParent(), resourceType);
    }
    return null;
  }
}
