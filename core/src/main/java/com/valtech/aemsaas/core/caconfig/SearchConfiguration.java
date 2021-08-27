package com.valtech.aemsaas.core.caconfig;

import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Search Configuration", description = "Search Configuration is used for all search requests done for the defined content tree.")
public @interface SearchConfiguration {

  @Property(label = "Search Index", description = "SaaS index (Required)")
  String index() default "N/A";

  @Property(label = "Search Field Name - Values", description = "Base Filters (Optional)")
  SearchFilterConfiguration[] searchFilters();

}