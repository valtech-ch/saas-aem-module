package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.query.Filter;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;

/**
 * Represents a service that performs fulltext search queries and retrieves the according results.
 */
public interface FulltextSearchService {

  /**
   * Gets search results according the context aware configuration.
   *
   * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
   * @param language            full text search language scope.
   * @param start               the start page for search results.
   * @param rows                the number of results per page
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
      @NonNull String language, int start, int rows) {
    return getResults(searchConfiguration, language, start, rows, Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
   * @param searchText          full text query value.
   * @param language            full text search language scope.
   * @param start               the start page for search results.
   * @param rows                the number of results per page
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
      String searchText, @NonNull String language, int start,
      int rows) {
    return getResults(searchConfiguration, searchText, language, start, rows, Collections.emptySet(),
        Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
   * @param searchText          full text query value.
   * @param language            full text search language scope.
   * @param start               the start page for search results.
   * @param rows                the number of results per page
   * @param filters             additional filters of type SimpleFilter or CompositeFilter
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
      String searchText, @NonNull String language, int start,
      int rows, Set<Filter> filters) {
    return getResults(searchConfiguration, searchText, language, start, rows, filters, Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
   * @param language            full text search language scope.
   * @param start               the start page for search results.
   * @param rows                the number of results per page
   * @param filters             additional filters of type SimpleFilter or CompositeFilter
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
      @NonNull String language, int start, int rows,
      Set<Filter> filters) {
    return getResults(searchConfiguration, language, start, rows, filters, Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
   * @param start               the start page for search results.
   * @param rows                the number of results per page
   * @param filters             additional filters of type SimpleFilter or CompositeFilter
   * @param facets              list of field names.
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
      @NonNull String language, int start, int rows,
      Set<Filter> filters,
      Set<String> facets) {
    return getResults(searchConfiguration, null, language, start, rows, filters, facets);
  }

  /**
   * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
   * @param searchText          full text query value.
   * @param language            full text search language scope.
   * @param start               the start page for search results.
   * @param rows                the number of results per page
   * @param filters             additional filters of type SimpleFilter or CompositeFilter
   * @param facets              list of field names.
   * @return search results object
   */
  Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
      String searchText, @NonNull String language, int start, int rows,
      Set<Filter> filters, Set<String> facets);

}
