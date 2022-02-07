package com.valtech.aem.saas.core.caconfig;

import com.day.cq.i18n.I18n;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.fulltextsearch.FilterConfiguration;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A sling model that provides context-aware search configurations.
 */
@Slf4j
@Model(adaptables = Resource.class,
       adapters = SearchCAConfigurationModel.class)
public final class SearchCAConfigurationModelImpl implements SearchCAConfigurationModel {

    public static final String I18N_SEARCH_CA_CONFIGURATION_INDEX_REQUIRED =
            "com.valtech.aem.saas.core.search.caConfiguration.indexRequiredMessage";

    @Self
    private Resource resource;

    private SearchConfiguration searchConfiguration;

    @OSGiService
    private I18nProvider i18nProvider;

    private I18n i18n;

    @PostConstruct
    private void init() {
        i18n = i18nProvider.getI18n(getLocale());
        ConfigurationBuilder configurationBuilder = resource.adaptTo(ConfigurationBuilder.class);
        if (configurationBuilder == null) {
            throw new IllegalStateException(String.format("Failed to resolve '%s' from resource: %s.",
                                                          SearchConfiguration.class.getName(),
                                                          resource.getPath()));
        }
        searchConfiguration = configurationBuilder.as(SearchConfiguration.class);
    }

    @Override
    public String getIndex() {
        if (StringUtils.isBlank(searchConfiguration.index())) {
            throw new IllegalStateException(i18n.get(I18N_SEARCH_CA_CONFIGURATION_INDEX_REQUIRED));
        }
        return searchConfiguration.index();
    }

    @Override
    public int getProjectId() {
        return searchConfiguration.projectId();
    }

    @Override
    public Set<Filter> getFilters() {
        return asStream(searchConfiguration.searchFilters())
                .map(CaFilterConfigurationModel::new)
                .filter(FilterConfiguration::isValid)
                .map(FilterConfiguration::getFilter)
                .collect(Collectors.toSet());
    }

    @Override
    public String getHighlightTagName() {
        return searchConfiguration.highlightTagName();
    }

    @Override
    public boolean isBestBetsEnabled() {
        return searchConfiguration.enableBestBets();
    }

    @Override
    public boolean isAutoSuggestEnabled() {
        return searchConfiguration.enableAutoSuggest();
    }

    private <T> Stream<T> asStream(T[] array) {
        return Optional.ofNullable(array).map(Arrays::stream).orElse(Stream.empty());
    }

    private Locale getLocale() {
        return Optional.ofNullable(resource.adaptTo(ResourceWrapper.class)).map(ResourceWrapper::getLocale)
                       .orElse(Locale.getDefault());
    }
}
