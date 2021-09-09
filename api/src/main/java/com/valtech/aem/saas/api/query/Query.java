package com.valtech.aem.saas.api.query;

import java.util.List;
import org.apache.http.NameValuePair;

/**
 * Represents a container of get query items. Items are of type {@link NameValuePair}.
 */
public interface Query {

  /**
   * Returns a list of get query items.
   *
   * @return list od items.
   */
  List<NameValuePair> getEntries();
}
