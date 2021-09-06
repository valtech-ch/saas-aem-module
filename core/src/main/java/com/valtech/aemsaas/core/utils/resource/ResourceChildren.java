package com.valtech.aemsaas.core.utils.resource;

import com.valtech.aemsaas.core.utils.StreamUtils;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.apache.sling.api.resource.Resource;

@RequiredArgsConstructor
public final class ResourceChildren {

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
