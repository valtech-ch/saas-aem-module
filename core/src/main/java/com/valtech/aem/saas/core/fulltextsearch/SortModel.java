package com.valtech.aem.saas.core.fulltextsearch;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * A POJO that represents a configurable sort parameter and provides sling resource binding.
 */
@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SortModel {

  @Getter
  @ValueMapValue
  private String fieldName;

  @Getter
  @ValueMapValue
  private String direction;
}
