package com.valtech.aem.saas.api.autocomplete;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.query.Filter;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

/**
 * Service that consumes the SaaS typeahead (autocomplete) api.
 */
public interface AutocompleteService {

    /**
     * Retrieves typeahead (autocomplete) results
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param text                search term.
     * @param language            search language scope.
     * @param filters             search filters
     * @return List of string represented autocomplete options. Empty list if no options are found.
     */
    default List<String> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            @NonNull String text,
            @NonNull String language,
            Set<Filter> filters){
        return getResults(searchConfiguration, text, language, filters, false);
    }

    /**
     * Retrieves typeahead (autocomplete) results
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param text                search term.
     * @param language            search language scope.
     * @param filters             search filters
     * @param disableContextFilters flag to disable the filters configured in context aware configuration.
     * @return List of string represented autocomplete options. Empty list if no options are found.
     */
    List<String> getResults(
        @NonNull SearchCAConfigurationModel searchConfiguration,
        @NonNull String text,
        @NonNull String language,
        Set<Filter> filters,
        boolean disableContextFilters);
}
