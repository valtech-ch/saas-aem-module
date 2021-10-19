package com.valtech.aem.saas.api.caconfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Search Configuration", description = "Search Configuration is used for all search requests done for the defined content tree.")
public @interface SearchConfiguration {

  boolean DEFAULT_ENABLE_BEST_BETS = false;
  boolean DEFAULT_ENABLE_AUTO_SUGGEST = true;

  @Property(label = "Search Index",
      description = "SaaS index (Required)")
  String index() default StringUtils.EMPTY;

  @Property(label = "Search Client",
      description = "SaaS client (Required)")
  String client() default StringUtils.EMPTY;

  @Property(label = "Search Field Name - Values",
      description = "Base Filters (Optional)")
  SearchFilterConfiguration[] searchFilters() default {};

  @Property(label = "Search Templates",
      description = "List of custom query templates' names, for specialized/different field boosting strategies. (Optional)")
  String[] templates() default {};

  @Property(label = "Enable Best Bets",
      description = "Flag that enables displaying best bets on the top of the search results.")
  boolean enableBestBets() default DEFAULT_ENABLE_BEST_BETS;

  @Property(label = "Enable Best Bets",
      description = "Flag that enables auto suggest feature in the search component.")
  boolean enableAutoSuggest() default DEFAULT_ENABLE_AUTO_SUGGEST;
}
