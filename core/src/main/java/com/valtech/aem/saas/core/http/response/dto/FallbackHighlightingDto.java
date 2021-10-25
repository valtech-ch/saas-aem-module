package com.valtech.aem.saas.core.http.response.dto;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FallbackHighlightingDto extends HighlightingDto {

  private static FallbackHighlightingDto instance;

  public static synchronized FallbackHighlightingDto getInstance() {
    if (instance == null) {
      instance = new FallbackHighlightingDto();
    }
    return instance;
  }

  @Override
  public Map<String, Map<String, List<String>>> getItems() {
    return Collections.emptyMap();
  }
}
