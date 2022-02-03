package com.valtech.aem.saas.core.fulltextsearch;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.inject.Inject;

/**
 * A POJO for a multi-value filter entry resource.
 */
@Model(adaptables = Resource.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterConfigurationModel extends MultiValueFilterConfiguration {

    @Inject
    public FilterConfigurationModel(@ValueMapValue(name = "name") String name,
                                    @ValueMapValue(name = "value") String value) {
        super(name, value);
    }

}
