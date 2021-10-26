package com.valtech.aem.saas.api.fulltextsearch.dto;

import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.OptionalQuery;
import com.valtech.aem.saas.api.query.TermQuery;
import java.util.List;

/**
 * Represents a payload object for the fulltext search request.
 */
public interface FulltextSearchPayloadDTO {

  /**
   * Return required term query.
   *
   * @return term query.
   */
  TermQuery getTermQuery();

  /**
   * Return required language query.
   *
   * @return language query.
   */
  LanguageQuery getLanguageQuery();

  /**
   * Returns list of optional query items.
   *
   * @return list of optional queries.
   */
  List<OptionalQuery> getOptionalQueries();
}
