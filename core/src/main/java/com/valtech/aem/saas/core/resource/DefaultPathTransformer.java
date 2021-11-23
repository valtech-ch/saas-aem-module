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

  public static final String HTML_EXTENSION = ".html";

  @Reference
  private Externalizer externalizer;

  @Override
  public List<String> externalizeList(SlingHttpServletRequest request, String path) {
    return Collections.singletonList(externalize(request, path));
  }

  @Override
  public String externalize(SlingHttpServletRequest request, String path) {
    return externalizer.publishLink(request.getResourceResolver(), path) + HTML_EXTENSION;
  }

  @Override
  public String map(SlingHttpServletRequest request, String path) {
    return externalizer.relativeLink(request, path);
  }
}
