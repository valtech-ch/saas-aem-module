package com.valtech.aem.saas.core.util;

import lombok.NonNull;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

public final class UniqueUtils {

  private UniqueUtils() {
    throw new UnsupportedOperationException();
  }

  public static final String SAAS_ID_PREFIX = "saas_";

  public static String getUniqueId(@NonNull String input) {
    return StringUtils.isNotBlank(input) ? SAAS_ID_PREFIX + DigestUtils.md5Hex(input).substring(0, 8)
        : StringUtils.EMPTY;
  }
}
