package com.valtech.aem.saas.api.resource;

import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Service interface for resource path mapping and externalizing.
 */
public interface PathTransformer {

  /**
   * Gets one or more externalized links for the passed path argument. It retrieves more than one item in case an
   * internal path can be represented by more than one url. (e.g. for indexing). Links are appended with the html
   * extension
   *
   * @param request sling request used for context.
   * @param path    resource/page path to be externalized.
   * @return list of externalized paths
   */
  List<String> externalizeList(SlingHttpServletRequest request, String path);

  /**
   * Gets one or more externalized links for the passed path argument. It retrieves more than one item in case an
   * internal path can be represented by more than one url. (e.g. for indexing) Links are appended with the html
   * extension
   *
   * @param path resource/page path to be externalized.
   * @return list of externalized paths
   */
  default List<String> externalizeList(String path) {
    return externalizeList(null, path);
  }

  /**
   * Gets an externalized link for the passed path argument. Link is appended with the html extension
   *
   * @param request sling request used for context.
   * @param path    resource/page path to be externalized.
   * @return externalized path
   */
  String externalize(SlingHttpServletRequest request, String path);

  /**
   * Retrieves a path that is a result of the applied mapping rules.
   *
   * @param request sling request used for context.
   * @param path    the resource's path on which the mapping rules are applied.
   * @return mapped path.
   */
  String map(SlingHttpServletRequest request, String path);
}
