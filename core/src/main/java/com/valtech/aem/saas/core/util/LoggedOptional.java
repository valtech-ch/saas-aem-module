package com.valtech.aem.saas.core.util;

import java.util.Optional;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;

@Slf4j
public final class LoggedOptional {

  public static <T> Optional<T> of(T nullable, Consumer<Logger> logger) {
    if (nullable == null) {
      logger.accept(log);
    }
    return Optional.ofNullable(nullable);
  }

  private LoggedOptional() {
    throw new UnsupportedOperationException();
  }
}
