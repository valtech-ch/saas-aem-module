package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.PathTransformer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;

/**
 * Implementation for unit testing purposes.
 */
@Component(service = PathTransformer.class)
public class BarBazPathTransformer implements PathTransformer {

  @Override
  public List<String> externalize(String path) {
    return StringUtils.equals(path, "bar")
        ? Arrays.asList("bar", "baz")
        : Collections.singletonList(path);
  }

  @Override
  public String map(SlingHttpServletRequest request, String path) {
    return "baz";
  }
}
