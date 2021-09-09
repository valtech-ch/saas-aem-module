package com.valtech.aem.saas.api.caconfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Property;

/**
 * CA Configuration for search filter entries. Nested in {@link SearchConfiguration}
 */
public @interface SearchFilterConfiguration {

    @Property(label = "Search Field Name", description = "Saas Field Name")
    String name() default StringUtils.EMPTY;

    @Property(label = "Search Field Value", description = "Saas Field Value")
    String value() default StringUtils.EMPTY;

}
