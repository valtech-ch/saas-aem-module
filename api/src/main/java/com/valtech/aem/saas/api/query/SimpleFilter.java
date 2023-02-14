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

    /**
     * The prefix character used to identify a startsWith filter in the search value.
     * Example: 'Adve*' to search for "Adventure" and "Advertisement"
     */
    public static final String FILTER_STARTS_WITH_SUFFIX = "*";
    private static final String FILTER_FIELD_VALUE_DELIMITER = ":";

    protected String name;
    protected String value;
    protected boolean startsWith;

    /**
     * Constructor for convenience purposes like testing only
     */
    public SimpleFilter(String name, String value) {
        this.name = name;
        this.value = value;
        this.startsWith = value != null && value.endsWith(FILTER_STARTS_WITH_SUFFIX);
    }

    @Override
    public String getQueryString() {
        if (StringUtils.isNoneBlank(name, value) && !FILTER_STARTS_WITH_SUFFIX.equals(value)) {
            return String.join(FILTER_FIELD_VALUE_DELIMITER, name, getSafeValue(value));
        }
        return StringUtils.EMPTY;
    }

    protected String getSafeValue(@NonNull String value) {
        if (startsWith && value.endsWith(FILTER_STARTS_WITH_SUFFIX)) {
            // escaping values because we don't have quotes
            return value.replace("/", "\\/").replace(":", "\\:");
        }

        // not startsWith case
        if (StringUtils.containsWhitespace(value)) {
            return StringUtils.wrapIfMissing(value, "\"");
        }
        return value;
    }
}
