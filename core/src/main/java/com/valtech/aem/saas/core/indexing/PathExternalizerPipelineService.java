package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.PathExternalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Slf4j
@Component(name = "Search as a Service - Path Externalizer Pipeline",
    service = PathExternalizerPipeline.class)
public class PathExternalizerPipelineService implements PathExternalizerPipeline {

  private final Map<Integer, PathExternalizer> pathExternalizerMap = new TreeMap<>();

  @Reference(cardinality = ReferenceCardinality.MULTIPLE,
      policy = ReferencePolicy.DYNAMIC)
  protected synchronized void bindPathExternalizer(final PathExternalizer pathExternalizer) {
    if (!pathExternalizerMap.containsKey(pathExternalizer.getRank())) {
      pathExternalizerMap.put(pathExternalizer.getRank(), pathExternalizer);
    } else {
      log.error("{} will be ignored because a path externalizer with rank: {} is already registered.",
          pathExternalizer.getClass(), pathExternalizer.getRank());
    }
  }

  protected synchronized void unbindPathExternalizer(final PathExternalizer pathExternalizer) {
    pathExternalizerMap.remove(pathExternalizer.getRank());
  }

  @Override
  public List<String> getExternalizedPaths(String path) {
    List<String> paths = Collections.singletonList(path);
    List<PathExternalizer> pathExternalizerList = new ArrayList<>(pathExternalizerMap.values());
    for (PathExternalizer pathExternalizer : pathExternalizerList) {
      paths = pathExternalizer.externalize(paths);
    }
    return paths;
  }

}
