package com.valtech.aemsaas.core.services.configuration;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.caconfig.annotation.Configuration;
import org.apache.sling.caconfig.annotation.Property;

@Configuration(label = "Base AEM Saas - Configuration", description = "Base AEM Saas Configuration")
public @interface BaseSaasConfiguration {

    @Property(label = "SaaS Index", description = "SaaS index (Required)")
    String saasIndex() default StringUtils.EMPTY;

    @Property(label = "Report Suite ID", description = "Base Filters (Optional)")
    BaseFilterConfiguration[] baseFilters();
}