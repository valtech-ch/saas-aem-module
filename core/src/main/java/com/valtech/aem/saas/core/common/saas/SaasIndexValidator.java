package com.valtech.aem.saas.core.common.saas;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SaasIndexValidator {

  private static SaasIndexValidator instance;

  public static synchronized SaasIndexValidator getInstance() {
    if (instance == null) {
      instance = new SaasIndexValidator();
    }
    return instance;
  }

  public void validate(String index) {
    if (StringUtils.isBlank(index)) {
      throw new IllegalArgumentException(
          "SaaS index name is missing. Please configure index name in Context Aware configuration.");
    }
  }

}
