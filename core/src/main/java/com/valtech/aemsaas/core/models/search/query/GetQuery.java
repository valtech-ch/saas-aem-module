package com.valtech.aemsaas.core.models.search.query;

import java.util.List;
import org.apache.http.NameValuePair;

/**
 * Represents a container of get query items. Items are of type {@link NameValuePair}.
 */
public interface GetQuery {

  /**
   * Returns a list of get query items.
   *
   * @return list od items.
   */
  List<NameValuePair> getEntries();
}
