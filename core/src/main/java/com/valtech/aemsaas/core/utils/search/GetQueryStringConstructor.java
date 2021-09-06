package com.valtech.aemsaas.core.utils.search;

import com.valtech.aemsaas.core.models.search.query.GetQuery;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import org.apache.http.client.utils.URIBuilder;

/**
 * Helper class that constructs a query string from a list of {@link GetQuery} objects. It uses {@link URIBuilder} to
 * take advantage of the query params escaping feature.
 */
@Builder
public class GetQueryStringConstructor {

  @Singular
  private List<GetQuery> queries;


  /**
   * Returns the query string.
   *
   * @return query string.
   */
  public String getQueryString() {
    URIBuilder uriBuilder = new URIBuilder();
    queries.stream()
        .map(GetQuery::getEntries)
        .forEach(uriBuilder::addParameters);
    return uriBuilder.toString();
  }
}
