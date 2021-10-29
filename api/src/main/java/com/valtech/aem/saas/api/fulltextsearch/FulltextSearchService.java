package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import lombok.NonNull;
import org.apache.sling.api.resource.Resource;

/**
 * Represents a service that performs fulltext search queries and retrieves the according results.
 */
public interface FulltextSearchService {

  /**
   * Gets search results according the context aware configuration.
   *
   * @param context resource with context aware configuration.
   * @param start   the start page for search results.
   * @param rows    the number of results per page
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull Resource context, int start, int rows) {
    return getResults(context, start, rows, Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param context    resource with context aware configuration.
   * @param searchText full text query value.
   * @param start      the start page for search results.
   * @param rows       the number of results per page
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull Resource context, String searchText, int start,
      int rows) {
    return getResults(context, searchText, start, rows, Collections.emptySet(), Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param context    resource with context aware configuration.
   * @param searchText full text query value.
   * @param start      the start page for search results.
   * @param rows       the number of results per page
   * @param filters    additional filters of type SimpleFilter or CompositeFilter
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull Resource context, String searchText, int start,
      int rows, Set<FilterModel> filters) {
    return getResults(context, start, rows, filters, Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param context resource with context aware configuration.
   * @param start   the start page for search results.
   * @param rows    the number of results per page
   * @param filters additional filters of type SimpleFilter or CompositeFilter
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull Resource context, int start, int rows,
      Set<FilterModel> filters) {
    return getResults(context, start, rows, filters, Collections.emptySet());
  }

  /**
   * Gets search results according the context aware configuration.
   *
   * @param context resource with context aware configuration.
   * @param start   the start page for search results.
   * @param rows    the number of results per page
   * @param filters additional filters of type SimpleFilter or CompositeFilter
   * @param facets  list of field names.
   * @return search results object
   */
  default Optional<FulltextSearchResultsDTO> getResults(@NonNull Resource context, int start, int rows,
      Set<FilterModel> filters,
      Set<String> facets) {
    return getResults(context, null, start, rows, filters, facets);
  }

  /**
   * @param context    resource with context aware configuration.
   * @param searchText full text query value.
   * @param start      the start page for search results.
   * @param rows       the number of results per page
   * @param filters    additional filters of type SimpleFilter or CompositeFilter
   * @param facets     list of field names.
   * @return search results object
   */
  Optional<FulltextSearchResultsDTO> getResults(@NonNull Resource context, String searchText, int start, int rows,
      Set<FilterModel> filters, Set<String> facets);

}
