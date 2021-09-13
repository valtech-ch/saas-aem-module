package com.valtech.aem.saas.core.util;

import java.util.OptionalInt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * Util class that offers a convenient mechanism of converting a string to integer.
 */
@Slf4j
@RequiredArgsConstructor
public final class StringToInteger {

  private final String value;

  /**
   * @return int optional, which is empty if the string input is of invalid number format.
   */
  public OptionalInt asInt() {
    if (value != null) {
      try {
        return OptionalInt.of(NumberUtils.createInteger(value));
      } catch (NumberFormatException e) {
        log.warn("String value [{}] is not a number", value, e);
      }
    }
    return OptionalInt.empty();
  }
}
