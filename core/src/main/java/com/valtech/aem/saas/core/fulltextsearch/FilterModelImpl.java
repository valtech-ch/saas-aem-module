package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.query.SimpleFilter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * A POJO that represents a search filter entry and provides sling resource binding.
 */
@Model(adaptables = Resource.class,
    adapters = FilterModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@NoArgsConstructor
@EqualsAndHashCode
public class FilterModelImpl extends SimpleFilter implements FilterModel {

  @Getter
  @ValueMapValue
  private String name;

  @Getter
  @ValueMapValue
  private String value;

  public FilterModelImpl(String name, String value) {
    super(name, value);
  }
}
