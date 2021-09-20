package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.Filter;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
    adapters = Filter.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class FilterImpl implements Filter {

  @Getter
  @ValueMapValue
  private String name;

  @Getter
  @ValueMapValue
  private String value;

}
