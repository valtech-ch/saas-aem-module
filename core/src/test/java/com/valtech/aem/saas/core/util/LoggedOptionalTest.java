package com.valtech.aem.saas.core.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.function.Consumer;

import static org.mockito.Mockito.*;

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
        LoggedOptional.of(null, logger);
        verify(logger, times(1)).accept(Mockito.any());
    }
}
