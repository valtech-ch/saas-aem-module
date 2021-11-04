package com.valtech.aem.saas.api.typeahead;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import java.util.List;
import java.util.Set;
import lombok.NonNull;

/**
 * Represents a service that consumes the SaaS typeahead api.
 */
public interface TypeaheadService {

  /**
   * Retrieves typeahead results
   *
   * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
   * @param text                search term.
   * @param language            search language scope.
   * @param filters             search filters
   * @return List of string represented typeahead options. Empty list if no options are found.
   */
  List<String> getResults(@NonNull SearchCAConfigurationModel searchConfiguration, @NonNull String text,
      @NonNull String language, Set<FilterModel> filters);

}
