package com.valtech.aem.saas.api.indexing;

import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Service interface for resource path mapping and externalizing.
 */
public interface PathTransformer {

  /**
   * Gets a one or more externalized links for the passed path argument.
   *
   * @param request sling request used for context.
   * @param path    resource/page path to be externalized.
   * @return list of externalized paths
   */
  List<String> externalize(SlingHttpServletRequest request, String path);

  default List<String> externalize(String path) {
    return externalize(null, path);
  }

  /**
   * Retrieves a path that is product of the applied mapping rules.
   *
   * @param request sling request used for context.
   * @param path    the resource's path on which the mapping rules are applied.
   * @return mapped path.
   */
  String map(SlingHttpServletRequest request, String path);
}
