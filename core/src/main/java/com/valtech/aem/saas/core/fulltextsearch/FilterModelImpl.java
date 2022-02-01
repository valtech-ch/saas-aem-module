package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FilterFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

/**
 * A POJO that represents a search filter entry and provides sling resource binding.
 */
@Model(adaptables = Resource.class,
       adapters = FilterModel.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterModelImpl implements FilterModel {

    @ValueMapValue
    private String name;

    @ValueMapValue
    private String value;

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
