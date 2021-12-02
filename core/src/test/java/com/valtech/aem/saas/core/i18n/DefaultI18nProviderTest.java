package com.valtech.aem.saas.core.i18n;

import org.apache.sling.i18n.ResourceBundleProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Locale;
import java.util.MissingResourceException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DefaultI18nProviderTest {

    @Mock
    ResourceBundleProvider resourceBundleProvider;

    @InjectMocks
    DefaultI18nProvider defaultI18nProvider;

    @Test
    void testGetI18n() {
        defaultI18nProvider.getI18n(Locale.ENGLISH);
        verify(resourceBundleProvider, times(1)).getResourceBundle(Locale.ENGLISH);
    }

    @Test
    void testGetI18n_missingResource() {
        Mockito.when(resourceBundleProvider.getResourceBundle(Locale.ENGLISH))
               .thenThrow(MissingResourceException.class);
        Assertions.assertThrows(MissingResourceException.class, () -> defaultI18nProvider.getI18n(Locale.ENGLISH));
    }
}
