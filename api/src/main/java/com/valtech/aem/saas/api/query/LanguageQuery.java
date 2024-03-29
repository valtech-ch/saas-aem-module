package com.valtech.aem.saas.api.query;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.Collections;
import java.util.List;

/**
 * Default implementation of {@link LanguageQuery}. It uses "lang" as the parameter key.
 */
public final class LanguageQuery implements Query {

    public static final String KEY = "lang";

    private final NameValuePair language;

    /**
     * Constructs a language query.
     *
     * @param value the language in 2 letter format.
     */
    public LanguageQuery(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Language value must not be blank.");
        }
        language = new BasicNameValuePair(KEY, value);
    }

    @Override
    public List<NameValuePair> getEntries() {
        return Collections.singletonList(language);
    }
}
