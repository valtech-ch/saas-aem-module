package com.valtech.aem.saas.core.bestbets;

import org.apache.commons.lang3.StringUtils;

public final class BestBetsApiCommonPathConstructor {

  public static final String URL_PATH_DELIMITER = "/";

  private final String baseUrl;
  private final String basePath;
  private final String apiVersionPath;

  public BestBetsApiCommonPathConstructor(String baseUrl, String basePath, String apiVersionPath) {
    if (StringUtils.isAnyBlank(baseUrl, basePath, apiVersionPath)) {
      throw new IllegalArgumentException("Constructor's params should not be blank.");
    }
    this.baseUrl = baseUrl;
    this.basePath = basePath;
    this.apiVersionPath = apiVersionPath;
  }

  public String getPath(String client) {
    if (StringUtils.isBlank(client)) {
      throw new IllegalArgumentException("Client value should not be blank.");
    }
    return StringUtils.join(
        baseUrl,
        basePath,
        URL_PATH_DELIMITER + client,
        apiVersionPath);
  }
}
