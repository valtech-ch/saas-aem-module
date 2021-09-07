package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.api.query.FulltextSearchOptionalQuery;
import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.TermQuery;
import com.valtech.aem.saas.core.query.GetQueryStringConstructor;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

/**
 * A default implementation of the full text search payload request. It requires specification of search term and
 * language.
 */
@Builder(builderMethodName = "hiddenBuilder")
public class DefaultFulltextSearchRequestPayload implements FulltextSearchGetRequestPayload {

  @NonNull
  private final TermQuery termQuery;
  @NonNull
  private final LanguageQuery languageQuery;
  @Singular
  private final List<FulltextSearchOptionalQuery> optionalQueries;

  public static DefaultFulltextSearchRequestPayloadBuilder builder(TermQuery termQuery,
      LanguageQuery languageQuery) {
    return hiddenBuilder().termQuery(termQuery).languageQuery(languageQuery);
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
}
