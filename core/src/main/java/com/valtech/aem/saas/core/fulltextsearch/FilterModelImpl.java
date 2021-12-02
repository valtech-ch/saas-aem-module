package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FilterFactory;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A POJO that represents a search filter entry and provides sling resource binding.
 */
@Model(adaptables = Resource.class,
        adapters = FilterModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterModelImpl implements FilterModel {

    public static final String FILTER_VALUES_SEPARATOR = ",";

    @Getter
    @ValueMapValue
    private String name;

    @Getter
    @ValueMapValue
    private String value;

    @Getter
    private Filter filter;

    @PostConstruct
    private void init() {
        if (isValid()) {
            filter = createFilter();
        }
    }

    private boolean isValid() {
        return StringUtils.isNoneBlank(name, value);
    }

    private Filter createFilter() {
        return FilterFactory.createFilter(name, getFilterValues());
    }

    private List<String> getFilterValues() {
        String[] values = StringUtils.split(value, FILTER_VALUES_SEPARATOR);
        return Arrays.stream(values).map(StringUtils::trim).collect(Collectors.toList());
    }

}
