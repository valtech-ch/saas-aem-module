package com.valtech.aem.saas.core.fulltextsearch;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * A POJO that represents a configurable facet and provides sling resource binding.
 */
@Model(adaptables = Resource.class,
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FacetModel {

    @Getter
    @ValueMapValue
    private String label;

    @Getter
    @ValueMapValue
    private String fieldName;

}
