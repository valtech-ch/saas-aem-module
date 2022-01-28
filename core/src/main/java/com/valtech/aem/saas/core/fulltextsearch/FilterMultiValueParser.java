package com.valtech.aem.saas.core.fulltextsearch;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public final class FilterMultiValueParser {
    private static final String FILTER_VALUES_SEPARATOR = ",";

    private final String value;

    public List<String> getValues() {
        if (value == null) {
            return Collections.emptyList();
        }
        String[] values = StringUtils.split(value, FILTER_VALUES_SEPARATOR);
        return Arrays.stream(values).map(StringUtils::trim).collect(Collectors.toList());
    }
}
