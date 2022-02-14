package com.valtech.aem.saas.api.caconfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Search Configuration",
               description = "Search Configuration is used for all search requests done for the defined content tree.")
public @interface SearchConfiguration {

    boolean DEFAULT_ENABLE_BEST_BETS = false;
    boolean DEFAULT_ENABLE_AUTO_SUGGEST = true;
    boolean DEFAULT_ENABLE_AUTO_COMPLETE = true;
    String DEFAULT_HIGHLIGHT_TAG_NAME = "em";
    int DEFAULT_PROJECT_ID = 1;
    int DEFAULT_AUTO_COMPLETE_TRIGGER_THRESHOLD = 3;
    int DEFAULT_AUTO_COMPLETE_RESULTS_MAX_LIMIT = 10;

    @Property(label = "Search Index",
              description = "SaaS index (Required)")
    String index() default StringUtils.EMPTY;

    @Property(label = "Search Project Id",
              description = "SaaS Project Id (Required)")
    int projectId() default DEFAULT_PROJECT_ID;

    @Property(label = "Search Field Name - Values",
              description = "Base Filters (Optional)")
    SearchFilterConfiguration[] searchFilters() default {};

    @Property(label = "Search result highlight tag name",
              description = "The name of the tag that will be used to highlight portions of text in the search " +
                      "results. (Optional)")
    String highlightTagName() default DEFAULT_HIGHLIGHT_TAG_NAME;

    @Property(label = "Enable Best Bets",
              description = "Flag that enables displaying best bets on the top of the search results.")
    boolean enableBestBets() default DEFAULT_ENABLE_BEST_BETS;

    @Property(label = "Enable Auto Suggest",
              description = "Flag that enables auto suggest feature in the search component.")
    boolean enableAutoSuggest() default DEFAULT_ENABLE_AUTO_SUGGEST;

    @Property(label = "Enable Auto Complete",
              description = "Flag that enables auto-complete (typeahead) feature in the search component.")
    boolean enableAutoComplete() default DEFAULT_ENABLE_AUTO_COMPLETE;

    @Property(label = "Auto-complete Trigger Threshold",
              description = "The minimum number of search input characters required for displaying auto-complete " +
                      "options.")
    int autoCompleteTriggerThreshold() default DEFAULT_AUTO_COMPLETE_TRIGGER_THRESHOLD;

    @Property(label = "Auto-complete options Max Limit",
              description = "The maximum number of auto-complete options displayed.")
    int autoCompleteOptionsMax() default DEFAULT_AUTO_COMPLETE_RESULTS_MAX_LIMIT;
}
