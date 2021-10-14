package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.PathExternalizer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Slf4j
@Component(name = "Search as a Service - Path Externalizer Pipeline",
    service = PathExternalizerPipeline.class)
public class PathExternalizerPipelineService implements PathExternalizerPipeline {

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policyOption = ReferencePolicyOption.GREEDY)
  private List<PathExternalizer> pathExternalizers = new CopyOnWriteArrayList<>();

  @Override
  public List<String> getExternalizedPaths(String path) {
    List<String> paths = Collections.singletonList(path);
    for (PathExternalizer pathExternalizer : pathExternalizers) {
      paths = pathExternalizer.externalize(paths);
    }
    return paths;
  }

}
