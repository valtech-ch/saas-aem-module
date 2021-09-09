package com.valtech.aem.saas.core.http.response;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FallbackHighlighting extends Highlighting {

  private static FallbackHighlighting instance;

  public static synchronized FallbackHighlighting getInstance() {
    if (instance == null) {
      instance = new FallbackHighlighting();
    }
    return instance;
  }

  @Override
  public Map<String, Map<String, List<String>>> getItems() {
    return Collections.emptyMap();
  }
}
