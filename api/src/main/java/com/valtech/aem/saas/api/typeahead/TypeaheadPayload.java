package com.valtech.aem.saas.api.typeahead;

import java.util.Map;

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
  Map<String, String> getFilterEntries();

}
