package com.valtech.aem.saas.core.common.resource;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.valtech.aem.saas.core.util.StreamUtils;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = Resource.class)
public class ResourceWrapper {

  @Self
  private Resource resource;

  public Optional<Resource> getParentWithResourceType(String resourceType) {
    return Optional.ofNullable(getParentWithResourceType(resource, resourceType));
  }

  public Stream<Resource> getDirectChildren() {
    return getDirectChildren(resource);
  }

  public Stream<Resource> getDescendents() {
    return getDescendents(resource);
  }

  public Optional<Page> getCurrentPage() {
    return Optional.ofNullable(resource.getResourceResolver().adaptTo(PageManager.class))
        .map(pm -> pm.getContainingPage(resource));
  }

  public Locale getLocale() {
    return getCurrentPage().map(p -> p.getLanguage(false)).orElse(Locale.getDefault());
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
