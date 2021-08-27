package com.valtech.aemsaas.core.caconfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Property;

public @interface SearchFilterConfiguration {

    @Property(label = "Search Field Name", description = "Saas Field Name")
    String name() default StringUtils.EMPTY;

    @Property(label = "Search Field Value", description = "Saas Field Value")
    String value() default StringUtils.EMPTY;

}
