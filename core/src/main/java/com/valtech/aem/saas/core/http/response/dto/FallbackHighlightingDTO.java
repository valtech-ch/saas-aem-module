package com.valtech.aem.saas.core.http.response.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FallbackHighlightingDTO extends HighlightingDTO {

  private static FallbackHighlightingDTO instance;

  public static synchronized FallbackHighlightingDTO getInstance() {
    if (instance == null) {
      instance = new FallbackHighlightingDTO();
    }
    return instance;
  }

  @Override
  public Map<String, Map<String, List<String>>> getItems() {
    return Collections.emptyMap();
  }
}
