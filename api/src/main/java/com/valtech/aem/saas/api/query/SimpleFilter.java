package com.valtech.aem.saas.api.query;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;


/**
 * An implementation of Filter that represents a simple filter query entry.
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SimpleFilter implements Filter {

    private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

    private String name;
    private String value;

    @Override
    public String getQueryString() {
        if (StringUtils.isNoneBlank(name, value)) {
            return String.join(FILTER_FIELD_VALUE_DELIMITER, name, getSafeValue(value));
        }
        return StringUtils.EMPTY;
    }

    private String getSafeValue(@NonNull String value) {
        if (StringUtils.containsWhitespace(value)) {
            return StringUtils.wrapIfMissing(value, "\"");
        }
        return value;
    }
}
