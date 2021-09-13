package com.valtech.aem.saas.core.util;

import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoggedOptionalTest {

  @Mock
  Consumer logger;

  @Mock
  Object nonnull;

  @Test
  void testLogOptional() {
    LoggedOptional.of(nonnull, logger);
    verify(logger, never()).accept(nonnull);
  }

  @Test
  void testLogOptional_logNull() {
    LoggedOptional.of(nonnull, logger);
    verify(logger, atMostOnce()).accept(null);
  }
}
