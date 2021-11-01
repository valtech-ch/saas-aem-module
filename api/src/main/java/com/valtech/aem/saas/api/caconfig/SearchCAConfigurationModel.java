package com.valtech.aem.saas.api.caconfig;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import java.util.List;

/**
 * A model that provides context-aware search configurations.
 */
public interface SearchCAConfigurationModel {

  /**
   * Retrieves configured index value. Index is considered required.
   *
   * @return saas index
   * @throws IllegalStateException thrown when index value is blank.
   */
  String getIndex();

  /**
   * Retrieves configured client value. Client is considered required.
   *
   * @return saas client
   * @throws IllegalStateException thrown when client value is blank.
   */
  String getClient();

  /**
   * Retrieves list of query ready items.
   *
   * @return list of filter entries.
   */
  List<FilterModel> getFilters();

  /**
   * Retrieves a list of predefined query templates.
   *
   * @return template names list
   */
  List<String> getTemplates();

  /**
   * Checks whether best bets feature is enabled.
   *
   * @return true if enabled.
   */
  boolean isBestBetsEnabled();

  /**
   * Checks whether auto suggest feature is enabled.
   *
   * @return true if enabled.
   */
  boolean isAutoSuggestEnabled();
}
