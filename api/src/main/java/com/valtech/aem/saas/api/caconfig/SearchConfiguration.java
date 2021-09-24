package com.valtech.aem.saas.api.caconfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Search Configuration", description = "Search Configuration is used for all search requests done for the defined content tree.")
public @interface SearchConfiguration {

  @Property(label = "Search Index", description = "SaaS index (Required)")
  String index() default StringUtils.EMPTY;

  @Property(label = "Search Client", description = "SaaS client (Required)")
  String client() default StringUtils.EMPTY;

  @Property(label = "Search Field Name - Values", description = "Base Filters (Optional)")
  SearchFilterConfiguration[] searchFilters() default {};

  @Property(label = "Search Templates", description = "List of custom query templates' names, for specialized/different field boosting strategies. (Optional)")
  String[] templates() default {};

  @Property(label = "Search Templates", description = "List of custom query templates' names, for specialized/different field boosting strategies. (Optional)")
  String highlightTagName() default StringUtils.EMPTY;

}
