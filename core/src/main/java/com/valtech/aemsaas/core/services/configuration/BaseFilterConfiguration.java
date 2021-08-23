package com.valtech.aemsaas.core.services.configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Base Filter - Configuration", description = "Base Filter Configuration")
public @interface BaseFilterConfiguration {

    @Property(label = "Saas Field Name", description = "Saas Field Name (Optional)")
    String fieldName() default StringUtils.EMPTY;

    @Property(label = "Saas Field Value", description = "Saas Field Value (Optional)")
    String fieldValue() default StringUtils.EMPTY;

}
