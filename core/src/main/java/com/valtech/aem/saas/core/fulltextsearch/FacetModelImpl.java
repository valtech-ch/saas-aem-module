package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FacetModel;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * A POJO that represents a configurable facet and provides sling resource binding.
 */
@Model(adaptables = Resource.class,
    adapters = FacetModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FacetModelImpl implements FacetModel {

  @Getter
  @ValueMapValue
  private String label;

  @Getter
  @ValueMapValue
  private String fieldName;

}
