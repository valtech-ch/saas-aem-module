package com.valtech.aem.saas.core.i18n;

import com.day.cq.i18n.I18n;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.sling.i18n.ResourceBundleProvider;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Locale;

/**
 * Default implementation that utilizes the JcrResourceBundleProvider.
 */
@Slf4j
@Component(name = "Search as a Service - I18n Provider Service",
        service = I18nProvider.class)
public class DefaultI18nProvider implements I18nProvider {

    @Reference(target = "(component.name=org.apache.sling.i18n.impl.JcrResourceBundleProvider)")
    private ResourceBundleProvider resourceBundleProvider;

    @Override
    public I18n getI18n(@NonNull Locale locale) {
        return new I18n(resourceBundleProvider.getResourceBundle(locale));
    }
}
