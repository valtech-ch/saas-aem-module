package com.valtech.aem.saas.api.bestbets.dto;

import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of the {@link BestBetPayloadDTO} interface. It performs input param validation in object's
 * construction phase.
 */
@ToString
@Value
public class DefaultBestBetPayloadDTO implements BestBetPayloadDTO {

  String index;
  String url;
  String term;
  String language;

  public DefaultBestBetPayloadDTO(String index, String url, String term, String language) {
    if (StringUtils.isAnyBlank(index, url, term, language)) {
      throw new IllegalArgumentException("Set values for all payload properties.");
    }
    this.index = index;
    this.url = url;
    this.term = term;
    this.language = language;
  }
}
