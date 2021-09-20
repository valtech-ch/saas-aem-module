package com.valtech.aem.saas.core.common.resource;

import com.valtech.aem.saas.core.util.StreamUtils;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;

@RequiredArgsConstructor
public final class ResourceChildren {

  @NonNull
  private final Resource resource;

  public Stream<Resource> getDirectChildren() {
    return getDirectChildren(resource);
  }

  public Stream<Resource> getDescendents() {
    return getDescendents(resource);
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
