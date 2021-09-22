package com.valtech.aem.saas.core.indexing;

import java.util.List;

public interface PathExternalizerPipeline {

  List<String> getExternalizedPaths(String path);
}
