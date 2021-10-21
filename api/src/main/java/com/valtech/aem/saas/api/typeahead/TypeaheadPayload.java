package com.valtech.aem.saas.api.typeahead;

import com.valtech.aem.saas.api.fulltextsearch.Filter;
import java.util.Set;

/**
 * Represents type ahead request payload.
 */
public interface TypeaheadPayload {

  /**
   * Gets the typeahead search text.
   *
   * @return the text for which a typeahead search is performed.
   */
  String getText();

  /**
   * Gets the typeahead language parameter value. It is used in determining the solr index field which contains the
   * typeahead data.
   *
   * @return the language for which a typeahead search is performed.
   */
  String getLanguage();

  /**
   * Gets typeahead filter entries.
   *
   * @return filter entries used for filtering typeahead results.
   */
  Set<Filter> getFilters();

}
