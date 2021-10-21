package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.PathTransformer;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

@Slf4j
@Component(name = "Search as a Service - Path Transformer Pipeline",
    service = PathTransformerPipeline.class)
public class PathTransformerPipelineService implements PathTransformerPipeline {

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policyOption = ReferencePolicyOption.GREEDY)
  private List<PathTransformer> pathTransformers = new CopyOnWriteArrayList<>();

  @Override
  public List<String> getExternalizedPaths(String path) {
    List<String> paths = Collections.singletonList(path);
    for (PathTransformer pathTransformer : pathTransformers) {
      paths = pathTransformer.externalize(paths);
    }
    return paths;
  }

  @Override
  public String getMappedPath(SlingHttpServletRequest request, String path) {
    String mappedPath = path;
    for (PathTransformer pathTransformer : pathTransformers) {
      mappedPath = pathTransformer.map(request, mappedPath);
    }
    return mappedPath;
  }

}
