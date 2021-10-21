package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.PathTransformer;
import java.util.Arrays;
import java.util.List;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

/**
 * Implementation for unit testing purposes.
 */
@Component(service = PathTransformer.class)
public class BarQuxPathTransformer implements PathTransformer {

  @Override
  public List<String> externalize(String path) {
    return Arrays.asList("bar", "qux");
  }

  @Override
  public String map(SlingHttpServletRequest request, String path) {
    return "bar";
  }
}
