package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.api.query.FulltextSearchOptionalQuery;
import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.TermQuery;
import com.valtech.aem.saas.core.query.GetQueryStringConstructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

/**
 * A default implementation of the full text search payload request. It requires specification of search term and
 * language.
 */
public class DefaultFulltextSearchRequestPayload implements FulltextSearchGetRequestPayload {

  @Getter
  @NonNull
  private final TermQuery termQuery;

  @Getter
  @NonNull
  private final LanguageQuery languageQuery;

  @Getter
  @Singular
  private final List<FulltextSearchOptionalQuery> optionalQueries;

  private DefaultFulltextSearchRequestPayload(@NonNull TermQuery termQuery,
      @NonNull LanguageQuery languageQuery,
      List<FulltextSearchOptionalQuery> optionalQueries) {
    this.termQuery = termQuery;
    this.languageQuery = languageQuery;
    this.optionalQueries = optionalQueries;
  }

  @Override
  public String getPayload() {
    if (validate()) {
      return GetQueryStringConstructor.builder()
          .query(termQuery)
          .query(languageQuery)
          .queries(optionalQueries)
          .build()
          .getQueryString();
    }
    throw new IllegalStateException("Payload is not valid.");
  }

  @Override
  public boolean validate() {
    return !termQuery.getEntries().isEmpty() && !languageQuery.getEntries().isEmpty();
  }

  public static Builder builder(@NonNull TermQuery termQuery, @NonNull LanguageQuery languageQuery) {
    return new Builder(termQuery, languageQuery);
  }

  public static class Builder {

    @NonNull
    private final TermQuery termQuery;
    @NonNull
    private final LanguageQuery languageQuery;
    private final List<FulltextSearchOptionalQuery> optionalQueries;

    private Builder(@NonNull TermQuery termQuery, @NonNull LanguageQuery languageQuery) {
      this.termQuery = termQuery;
      this.languageQuery = languageQuery;
      this.optionalQueries = new ArrayList<>();
    }

    public Builder optionalQuery(FulltextSearchOptionalQuery optionalQuery) {
      this.optionalQueries.add(optionalQuery);
      return this;
    }

    public Builder optionalQueries(Collection<? extends FulltextSearchOptionalQuery> optionalQueries) {
      this.optionalQueries.addAll(optionalQueries);
      return this;
    }

    public Builder clearOptionalQueries() {
      this.optionalQueries.clear();
      return this;
    }

    public DefaultFulltextSearchRequestPayload build() {
      return new DefaultFulltextSearchRequestPayload(this.termQuery, this.languageQuery, this.optionalQueries);
    }
  }
}
