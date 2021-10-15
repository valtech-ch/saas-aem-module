package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.PathExternalizer;
import java.util.Arrays;
import java.util.List;
import org.osgi.service.component.annotations.Component;

/**
 * Implementation for unit testing purposes.
 */
@Component(service = PathExternalizer.class)
public class BarQuxPathExternalizer implements PathExternalizer {

  @Override
  public List<String> externalize(String path) {
    return Arrays.asList("bar", "qux");
  }
}
