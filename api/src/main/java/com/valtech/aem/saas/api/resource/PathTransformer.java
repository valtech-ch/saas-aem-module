package com.valtech.aem.saas.api.resource;

import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Service interface for resource path mapping and externalizing.
 */
public interface PathTransformer {

  /**
   * Gets one or more externalized links for the passed path argument. It retrieves more than one item in case an
   * internal path can be represented by more than 1 url. (e.g. for indexing)
   *
   * @param request      sling request used for context.
   * @param resourcePath resource/page path to be externalized.
   * @return list of externalized paths
   */
  List<String> externalize(SlingHttpServletRequest request, String resourcePath);

  default List<String> externalize(String resourcePath) {
    return externalize(null, resourcePath);
  }

  /**
   * Retrieves a path that is product of the applied mapping rules.
   *
   * @param request      sling request used for context.
   * @param resourcePath the resource's path on which the mapping rules are applied.
   * @return mapped path.
   */
  String map(SlingHttpServletRequest request, String resourcePath);
}
