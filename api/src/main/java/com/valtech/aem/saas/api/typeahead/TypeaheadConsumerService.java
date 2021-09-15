package com.valtech.aem.saas.api.typeahead;

import java.util.List;
import lombok.NonNull;

/**
 * Represents the typeahead consumer service for a specific index.
 */
public interface TypeaheadConsumerService {

  /**
   * Retrieves typeahead results
   *
   * @param typeaheadPayload object containing typeahead query values.
   * @return List of string represented typeahead options. Empty list if no options are found.
   */
  List<String> getResults(@NonNull TypeaheadPayload typeaheadPayload);
}
