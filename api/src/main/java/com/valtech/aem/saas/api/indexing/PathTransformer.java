package com.valtech.aem.saas.api.indexing;

import java.util.List;
import java.util.stream.Collectors;
import org.apache.sling.api.SlingHttpServletRequest;

/**
 * Service interface for externalizing resource path.
 */
public interface PathTransformer {

  /**
   * Gets a processed version of the passed path argument.
   *
   * @param path resource/page path to be externalized.
   * @return list of externalized paths
   */
  List<String> externalize(String path);

  default List<String> externalize(List<String> paths) {
    return paths.stream()
        .map(this::externalize)
        .flatMap(List::stream)
        .collect(Collectors.toList());
  }

  String map(SlingHttpServletRequest request, String path);
}
