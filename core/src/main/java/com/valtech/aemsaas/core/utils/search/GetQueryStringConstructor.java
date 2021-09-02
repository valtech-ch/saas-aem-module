package com.valtech.aemsaas.core.utils.search;

import com.valtech.aemsaas.core.models.search.GetQuery;
import java.util.List;
import lombok.Builder;
import lombok.Singular;
import org.apache.http.client.utils.URIBuilder;

@Builder
public class GetQueryStringConstructor {

  @Singular
  private List<GetQuery> queries;


  public String getQueryString() {
    URIBuilder uriBuilder = new URIBuilder();
    queries.stream()
        .map(GetQuery::getEntries)
        .forEach(uriBuilder::addParameters);
    return uriBuilder.toString();
  }
}
