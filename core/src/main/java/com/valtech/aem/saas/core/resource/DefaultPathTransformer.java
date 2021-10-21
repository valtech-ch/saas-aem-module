package com.valtech.aem.saas.core.resource;

import com.day.cq.commons.Externalizer;
import com.valtech.aem.saas.api.resource.PathTransformer;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * Default implementation of {@link PathTransformer} interface, that utilizes Day CQ Link Externalizer.
 */
@Slf4j
@Component(name = "Search as a Service - Default Path Transformer Service",
    service = PathTransformer.class)
public class DefaultPathTransformer implements PathTransformer {

  @Reference
  private Externalizer externalizer;

  @Override
  public List<String> externalize(SlingHttpServletRequest request, String resourcePath) {
    return Collections.singletonList(externalizer.publishLink(request.getResourceResolver(), resourcePath));
  }

  @Override
  public String map(SlingHttpServletRequest request, String resourcePath) {
    return externalizer.relativeLink(request, resourcePath);
  }
}
