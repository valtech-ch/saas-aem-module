package com.valtech.aemsaas.core.models.search;

import com.valtech.aemsaas.core.utils.search.GetQueryStringConstructor;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;

@Builder(builderMethodName = "hiddenBuilder")
public class DefaultFulltextSearchGetRequestPayload implements FulltextSearchGetRequestPayload {

  @NonNull
  private final TermQuery termQuery;
  @NonNull
  private final LanguageQuery languageQuery;
  @Singular
  private final List<FulltextSearchOptionalGetQuery> optionalQueries;

  public static DefaultFulltextSearchGetRequestPayloadBuilder builder(TermQuery termQuery,
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
