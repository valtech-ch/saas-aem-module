package com.valtech.aemsaas.core.models.sling.components.caconfigdemo;

import com.valtech.aemsaas.core.caconfig.SearchConfiguration;
import com.valtech.aemsaas.core.caconfig.SearchFilterConfiguration;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = Resource.class)
public class CaConfigDemo {

  @Self
  private Resource resource;

  @Getter
  private final SearchConfiguration searchConfiguration;

  public CaConfigDemo(Resource resource) {
    ConfigurationBuilder configurationBuilder = resource.adaptTo(ConfigurationBuilder.class);
    this.searchConfiguration = configurationBuilder.as(SearchConfiguration.class);
  }

  public SearchFilterConfiguration[] getSearchFilters() {
    return searchConfiguration.searchFilters();
  }
}
