package com.valtech.aem.saas.core.caconfig;

import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.core.fulltextsearch.FilterModelImpl;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;

/**
 * A helper that provides context-aware search configurations.
 */
public final class SearchConfigurationProvider {

  private final SearchConfiguration searchConfiguration;

  public SearchConfigurationProvider(@NonNull Resource resource) {
    ConfigurationBuilder configurationBuilder = resource.adaptTo(ConfigurationBuilder.class);
    if (configurationBuilder == null) {
      throw new IllegalArgumentException("Resource can not be adapted to ConfigurationBuilder.");
    }
    searchConfiguration = configurationBuilder.as(SearchConfiguration.class);
  }

  /**
   * Retrieves configured index value. Index is considered required.
   *
   * @return saas index
   * @throws IllegalStateException thrown when index value is blank.
   */
  public String getIndex() {
    if (StringUtils.isBlank(searchConfiguration.index())) {
      throw new IllegalStateException("Search Index must be configured.");
    }
    return searchConfiguration.index();
  }

  /**
   * Retrieves configured client value. Client is considered required.
   *
   * @return saas client
   * @throws IllegalStateException thrown when client value is blank.
   */
  public String getClient() {
    if (StringUtils.isBlank(searchConfiguration.client())) {
      throw new IllegalStateException("Search Client must be configured.");
    }
    return searchConfiguration.client();
  }


  /**
   * Retrieves set of query ready items.
   *
   * @return Set of filter entries.
   */
  public Set<FilterModel> getFilters() {
    return asStream(searchConfiguration.searchFilters())
        .map(searchFilterConfiguration -> new FilterModelImpl(searchFilterConfiguration.name(),
            searchFilterConfiguration.value()))
        .collect(Collectors.toSet());
  }

  /**
   * Retrieves a list of predefined query templates.
   *
   * @return template names list
   */
  public List<String> getTemplates() {
    return asStream(searchConfiguration.templates())
        .collect(Collectors.toList());
  }

  public String getHighlightTagName() {
    return searchConfiguration.highlightTagName();
  }

  /**
   * Checks whether best bets feature is enabled.
   *
   * @return true if enabled.
   */
  public boolean isBestBetsEnabled() {
    return searchConfiguration.enableBestBets();
  }

  /**
   * Checks whether auto suggest feature is enabled.
   *
   * @return true if enabled.
   */
  public boolean isAutoSuggestEnabled() {
    return searchConfiguration.enableAutoSuggest();
  }

  private <T> Stream<T> asStream(T[] array) {
    return Optional.ofNullable(array).map(Arrays::stream).orElse(Stream.empty());
  }

}
