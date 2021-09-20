package com.valtech.aem.saas.core.common.resource;

import com.valtech.aem.saas.core.util.StreamUtils;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;

/**
 * A helper class for consuming a resource.
 */
@RequiredArgsConstructor
public final class ResourceConsumer {

  @NonNull
  private final Resource resource;

  /**
   * Gets resource's ancestor of the specified resource type.
   *
   * @param resourceType ancestor's resource type
   * @return an ancestor resource.
   */
  public Optional<Resource> getParentWithResourceType(String resourceType) {
    return Optional.ofNullable(getParentWithResourceType(resource, resourceType));
  }

  /**
   * Gets resource's direct children.
   *
   * @return list of child resources.
   */
  public Stream<Resource> getDirectChildren() {
    return getDirectChildren(resource);
  }

  /**
   * Gets resource's descendants.
   *
   * @return list of descendant resources.
   */
  public Stream<Resource> getDescendents() {
    return getDescendents(resource);
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

  private Stream<Resource> getDirectChildren(Resource resource) {
    return StreamUtils.asStream(resource.listChildren());
  }

  private Stream<Resource> getDescendents(Resource resource) {
    return Stream.concat(
        getDirectChildren(resource),
        getDirectChildren(resource)
            .flatMap(this::getDescendents));
  }
}
