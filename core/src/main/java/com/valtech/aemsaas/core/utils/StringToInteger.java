package com.valtech.aemsaas.core.utils;

import java.util.OptionalInt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

@Slf4j
@RequiredArgsConstructor
public final class StringToInteger {

  private final String value;

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
