package com.valtech.aem.saas.core.indexing;

import java.util.List;

/**
 * Represents a service that applies one or more externalization rules to a path input in a pipeline manner.
 */
public interface PathExternalizerPipeline {

  /**
   * Gets list of paths as product od the applied externalization rules.
   *
   * @param path the resource path that is externalized
   * @return list of externalized paths.
   */
  List<String> getExternalizedPaths(String path);
}
