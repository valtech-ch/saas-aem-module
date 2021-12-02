package com.valtech.aem.saas.core.i18n;

import com.day.cq.i18n.I18n;
import lombok.NonNull;

import java.util.Locale;

/**
 * Service for instantiating {@link I18n} instance.
 */
public interface I18nProvider {

    /**
     * Gets instance of {@link I18n} that can be used for translation resolution.
     *
     * @param locale locale used for instantiating the proper {@link java.util.ResourceBundle}.
     * @return com.day.cq.i18n.I18n
     */
    I18n getI18n(@NonNull Locale locale);
}
