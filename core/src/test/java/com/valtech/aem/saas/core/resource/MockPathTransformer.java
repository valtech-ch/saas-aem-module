package com.valtech.aem.saas.core.resource;

import com.valtech.aem.saas.api.resource.PathTransformer;
import java.util.Collections;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;

public class MockPathTransformer implements PathTransformer {

  @Override
  public List<String> externalize(SlingHttpServletRequest request, String resourcePath) {
    return Collections.singletonList(resourcePath);
  }

  @Override
  public String map(SlingHttpServletRequest request, String resourcePath) {
    return resourcePath;
  }
}
