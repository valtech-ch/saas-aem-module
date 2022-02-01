package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FilterFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Abstract implementation of multi-value filer configuration.
 */
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class MultiValueFilterConfiguration implements FilterConfiguration {
    private final String name;
    private final String value;

    @Override
    public Filter getFilter() {
        List<String> values = new FilterMultiValueParser(value).getValues();
        return FilterFactory.createFilter(name, values);
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(name, value);
    }
}
