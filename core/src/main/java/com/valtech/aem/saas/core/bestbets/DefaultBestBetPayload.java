package com.valtech.aem.saas.core.bestbets;

import com.valtech.aem.saas.api.bestbets.BestBetPayload;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of the {@link BestBetPayload} interface. It employs the builder pattern for object creation,
 * and it performs an object validation when building the object.
 */
@ToString
@Value
public class DefaultBestBetPayload implements BestBetPayload {

  String index;
  String url;
  String term;
  String language;

  public DefaultBestBetPayload(String index, String url, String term, String language) {
    if (StringUtils.isAnyBlank(index, url, term, language)) {
      throw new IllegalArgumentException("Set values for all payload properties.");
    }
    this.index = index;
    this.url = url;
    this.term = term;
    this.language = language;
  }
}
