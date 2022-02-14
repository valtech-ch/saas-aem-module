package com.valtech.aem.saas.api.autocomplete;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.query.Filter;
import lombok.NonNull;

import java.util.List;
import java.util.Set;

/**
 * Service that consumes the SaaS typeahead (auto-complete) api.
 */
public interface AutoCompleteService {

    /**
     * Retrieves typeahead (auto-complete) results
     *
     * @param searchConfiguration sling model accessing context aware search configurations (i.e client and index).
     * @param text                search term.
     * @param language            search language scope.
     * @param filters             search filters
     * @return List of string represented auto-complete options. Empty list if no options are found.
     */
    List<String> getResults(
            @NonNull SearchCAConfigurationModel searchConfiguration,
            @NonNull String text,
            @NonNull String language,
            Set<Filter> filters);

}
