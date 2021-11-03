package com.valtech.aem.saas.core.caconfig;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.core.fulltextsearch.FilterModelImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

/**
 * A sling model that provides context-aware search configurations.
 */
@Slf4j
@Model(adaptables = Resource.class,
    adapters = SearchCAConfigurationModel.class)
public final class SearchCAConfigurationModelImpl implements SearchCAConfigurationModel {

  @Self
  private Resource resource;

  private SearchConfiguration searchConfiguration;

  @PostConstruct
  private void init() {
    ConfigurationBuilder configurationBuilder = resource.adaptTo(ConfigurationBuilder.class);
    if (configurationBuilder == null) {
      throw new IllegalStateException("Failed to resolve search configuration from adapted resource.");
    }
    searchConfiguration = configurationBuilder.as(SearchConfiguration.class);
  }

  @Override
  public String getIndex() {
    if (StringUtils.isBlank(searchConfiguration.index())) {
      throw new IllegalStateException("Search Index must be configured.");
    }
    return searchConfiguration.index();
  }

  @Override
  public String getClient() {
    if (StringUtils.isBlank(searchConfiguration.client())) {
      throw new IllegalStateException("Search Client must be configured.");
    }
    return searchConfiguration.client();
  }

  @Override
  public int getProjectId() {
    if (searchConfiguration.projectId() <= SearchConfiguration.DEFAULT_PROJECT_ID) {
      throw new IllegalStateException("Project Id must be configured and should be a non-negative integer.");
    }
    return searchConfiguration.projectId();
  }

  @Override
  public Set<FilterModel> getFilters() {
    return asStream(searchConfiguration.searchFilters())
        .map(searchFilterConfiguration -> new FilterModelImpl(searchFilterConfiguration.name(),
            searchFilterConfiguration.value()))
        .collect(Collectors.toSet());
  }

  @Override
  public List<String> getTemplates() {
    return asStream(searchConfiguration.templates())
        .collect(Collectors.toList());
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

}
