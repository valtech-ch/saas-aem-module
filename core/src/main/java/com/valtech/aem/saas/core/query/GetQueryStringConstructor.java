package com.valtech.aem.saas.core.query;

import com.valtech.aem.saas.api.query.Query;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import org.apache.http.client.utils.URIBuilder;

/**
 * Helper class that constructs a query string from a list of {@link Query} objects. It uses {@link URIBuilder} to take
 * advantage of the query params escaping feature.
 */
@Builder
public class GetQueryStringConstructor {

  @Singular
  private List<Query> queries;


  /**
   * Returns the query string.
   *
   * @return query string.
   */
  public String getQueryString() {
    URIBuilder uriBuilder = new URIBuilder();
    queries.stream()
        .map(Query::getEntries)
        .forEach(uriBuilder::addParameters);
    return uriBuilder.toString();
  }
}
