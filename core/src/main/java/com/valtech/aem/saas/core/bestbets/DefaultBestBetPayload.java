package com.valtech.aem.saas.core.bestbets;

import com.valtech.aem.saas.api.bestbets.BestBetPayload;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Value;
import org.apache.commons.lang3.StringUtils;

/**
 * Default implementation of the {@link BestBetPayload} interface. It employs the builder pattern for object creation,
 * and it performs an object validation when building the object.
 */
@ToString
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DefaultBestBetPayload implements BestBetPayload {

  String index;
  String url;
  String term;
  String language;

  public static Builder builder() {
    return new Builder();
  }

  @NoArgsConstructor(access = AccessLevel.PRIVATE)
  public static class Builder {

    private String index;
    private String url;
    private String term;
    private String language;

    public Builder index(String index) {
      this.index = index;
      return this;
    }

    public Builder url(String url) {
      this.url = url;
      return this;
    }

    public Builder term(String term) {
      this.term = term;
      return this;
    }

    public Builder language(String language) {
      this.language = language;
      return this;
    }

    public DefaultBestBetPayload build() {
      validate();
      return new DefaultBestBetPayload(index, url, term, language);
    }

    private void validate() {
      if (StringUtils.isAnyBlank(index, url, term, language)) {
        throw new IllegalStateException("Set values for all payload properties.");
      }
    }

  }
}
