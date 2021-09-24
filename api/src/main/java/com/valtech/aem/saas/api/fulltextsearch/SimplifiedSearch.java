package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.query.Filter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.sling.api.resource.Resource;

/**
 * Represents simplified search interface that automatically applies context aware search configuration.
 */
public interface SimplifiedSearch {

  /**
   * Gets search results according the context aware configuration.
   *
   * @param context resource with context aware configuration.
   * @param start   the start page for search results.
   * @param rows    the number of results per page
   * @return search results object
   */
  default Optional<FulltextSearchResults> getResults(Resource context, int start, int rows) {
    return getResults(context, start, rows, Collections.emptyList());
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
  default Optional<FulltextSearchResults> getResults(Resource context, String searchText, int start, int rows) {
    return getResults(context, searchText, start, rows, Collections.emptyList(), Collections.emptyList());
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
  default Optional<FulltextSearchResults> getResults(Resource context, int start, int rows, List<Filter> filters) {
    return getResults(context, start, rows, filters, Collections.emptyList());
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
  default Optional<FulltextSearchResults> getResults(Resource context, int start, int rows, List<Filter> filters,
      List<String> facets) {
    return getResults(context, null, start, rows, filters, facets);
  }

  /**
   * @param context    resource with context aware configuration.
   * @param searchText full text query value.
   * @param start
   * @param rows
   * @param filters    additional filters of type SimpleFilter or CompositeFilter
   * @param facets     list of field names.
   * @return search results object
   */
  Optional<FulltextSearchResults> getResults(Resource context, String searchText, int start, int rows,
      List<Filter> filters, List<String> facets);

}
