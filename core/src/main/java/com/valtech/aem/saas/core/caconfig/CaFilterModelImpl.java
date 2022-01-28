package com.valtech.aem.saas.core.caconfig;

import com.valtech.aem.saas.api.caconfig.SearchFilterConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FilterFactory;
import com.valtech.aem.saas.core.fulltextsearch.FilterMultiValueParser;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class CaFilterModelImpl implements FilterModel {
    @NonNull
    private final SearchFilterConfiguration searchFilterConfiguration;

    public Filter getFilter() {
        List<String> values = new FilterMultiValueParser(searchFilterConfiguration.value()).getValues();
        return FilterFactory.createFilter(searchFilterConfiguration.name(), values);
    }

    @Override
    public boolean isValid() {
        return StringUtils.isNoneBlank(searchFilterConfiguration.name(), searchFilterConfiguration.value());
    }
}
