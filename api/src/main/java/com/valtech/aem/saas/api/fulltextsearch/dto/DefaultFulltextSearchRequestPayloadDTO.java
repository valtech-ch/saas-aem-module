package com.valtech.aem.saas.api.fulltextsearch.dto;

import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.OptionalQuery;
import com.valtech.aem.saas.api.query.TermQuery;
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
public class DefaultFulltextSearchRequestPayloadDTO implements FulltextSearchPayloadDTO {

  @Getter
  @NonNull
  private final TermQuery termQuery;

  @Getter
  @NonNull
  private final LanguageQuery languageQuery;

  @Getter
  @Singular
  private final List<OptionalQuery> optionalQueries;

  private DefaultFulltextSearchRequestPayloadDTO(@NonNull TermQuery termQuery,
      @NonNull LanguageQuery languageQuery,
      List<OptionalQuery> optionalQueries) {
    this.termQuery = termQuery;
    this.languageQuery = languageQuery;
    this.optionalQueries = optionalQueries;
  }

  public static Builder builder(@NonNull TermQuery termQuery, @NonNull LanguageQuery languageQuery) {
    return new Builder(termQuery, languageQuery);
  }

  public static class Builder {

    @NonNull
    private final TermQuery termQuery;
    @NonNull
    private final LanguageQuery languageQuery;
    private final List<OptionalQuery> optionalQueries;

    private Builder(@NonNull TermQuery termQuery, @NonNull LanguageQuery languageQuery) {
      this.termQuery = termQuery;
      this.languageQuery = languageQuery;
      this.optionalQueries = new ArrayList<>();
    }

    public Builder optionalQuery(OptionalQuery optionalQuery) {
      this.optionalQueries.add(optionalQuery);
      return this;
    }

    public Builder optionalQueries(Collection<? extends OptionalQuery> optionalQueries) {
      this.optionalQueries.addAll(optionalQueries);
      return this;
    }

    public Builder clearOptionalQueries() {
      this.optionalQueries.clear();
      return this;
    }

    public DefaultFulltextSearchRequestPayloadDTO build() {
      return new DefaultFulltextSearchRequestPayloadDTO(this.termQuery, this.languageQuery, this.optionalQueries);
    }
  }
}
