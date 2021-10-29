package com.valtech.aem.saas.api.typeahead;

import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import java.util.List;
import java.util.Set;
import lombok.NonNull;
import org.apache.sling.api.resource.Resource;

/**
 * Represents a service that consumes the SaaS typeahead api.
 */
public interface TypeaheadService {

  /**
   * Retrieves typeahead results
   *
   * @param context resource that specifies the context. used for resolving the client and index parameters.
   * @param text    search term.
   * @param filters search filters
   * @return List of string represented typeahead options. Empty list if no options are found.
   */
  List<String> getResults(@NonNull Resource context, @NonNull String text, Set<FilterModel> filters);

}
