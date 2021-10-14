package com.valtech.aem.saas.core.indexing;

import com.valtech.aem.saas.api.indexing.PathExternalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;

/**
 * Implementation for unit testing purposes.
 */
@Component(service = PathExternalizer.class)
public class BarBazPathExternalizer implements PathExternalizer {

  @Override
  public List<String> externalize(String path) {
    return StringUtils.equals(path, "bar")
        ? Arrays.asList("bar", "baz")
        : Collections.singletonList(path);
  }
}
