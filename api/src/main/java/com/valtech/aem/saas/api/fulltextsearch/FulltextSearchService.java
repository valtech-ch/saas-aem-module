package com.valtech.aem.saas.api.fulltextsearch;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.Sort;
import java.util.List;
import lombok.NonNull;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.tuple.Pair;

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
    default Optional<FulltextSearchResultsDTO> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            @NonNull String language,
            int start,
            int rows) {
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
    default Optional<FulltextSearchResultsDTO> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            String searchText,
            @NonNull String language,
            int start,
            int rows) {
        return getResults(searchConfiguration, searchText, language, start, rows, Collections.emptySet(),
                          Collections.emptySet(), null);
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
    default Optional<FulltextSearchResultsDTO> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            String searchText,
            @NonNull String language,
            int start,
            int rows,
            Set<Filter> filters) {
        return getResults(searchConfiguration,
                          searchText,
                          language,
                          start,
                          rows,
                          filters,
                          Collections.emptySet(),
                          null);
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
    default Optional<FulltextSearchResultsDTO> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            @NonNull String language,
            int start,
            int rows,
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
    default Optional<FulltextSearchResultsDTO> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            @NonNull String language,
            int start,
            int rows,
            Set<Filter> filters,
            Set<String> facets) {
        return getResults(searchConfiguration, null, language, start, rows, filters, facets, null);
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
    @SuppressWarnings("java:S107")
    Optional<FulltextSearchResultsDTO> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            String searchText,
            @NonNull String language,
            int start,
            int rows,
            Set<Filter> filters,
            Set<String> facets,
            String template);

    /**
     * @param searchConfiguration   sling model accessing context aware search configurations (i.e client and index).
     * @param searchText            full text query value.
     * @param language              full text search language scope.
     * @param start                 the start page for search results.
     * @param rows                  the number of results per page
     * @param filters               additional filters of type SimpleFilter or CompositeFilter
     * @param facets                list of field names.
     * @param disableContextFilters flag to disable the filters configured in context aware configuration.
     * @param template              a template to filter by.
     * @return search results object
     */

    Optional<FulltextSearchResultsDTO> getResults(//NOSONAR
            @NonNull SearchCAConfigurationModel searchConfiguration,
            String searchText,
            @NonNull String language,
            int start,
            int rows,
            Set<Filter> filters,
            Set<String> facets,
            boolean disableContextFilters,
            String template);

    /**
     * @param searchConfiguration   sling model accessing context aware search configurations (i.e client and index).
     * @param searchText            full text query value.
     * @param language              full text search language scope.
     * @param start                 the start page for search results.
     * @param rows                  the number of results per page
     * @param filters               additional filters of type SimpleFilter or CompositeFilter
     * @param facets                list of field names.
     * @param disableContextFilters flag to disable the filters configured in context aware configuration.
     * @param template              a template to filter by.
     * @param sortParameters        a list of field - sort direction parameters used for sorting of results.
     * @return search results object
     */
    @SuppressWarnings("java:S107")
    Optional<FulltextSearchResultsDTO> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            String searchText,
            @NonNull String language,
            int start,
            int rows,
            Set<Filter> filters,
            Set<String> facets,
            boolean disableContextFilters,
            String template,
            List<Pair<String, Sort>> sortParameters);
}
