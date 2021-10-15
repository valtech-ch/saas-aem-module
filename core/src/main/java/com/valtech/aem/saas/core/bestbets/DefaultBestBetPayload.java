package com.valtech.aem.saas.core.bestbets;

import com.valtech.aem.saas.api.bestbets.BestBetPayload;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of the {@link BestBetPayload} interface. It performs an param validation in object's
 * construction phase.
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
