package com.valtech.aemsaas.core.utils.search;

import com.valtech.aemsaas.core.models.search.FulltextSearchGetQuery;
import com.valtech.aemsaas.core.models.search.GetQuery;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;

public abstract class GetQueryStringConstructor<T extends GetQuery> {

  protected abstract List<T> getQueries();

  public String getQueryString() {
    URIBuilder uriBuilder = new URIBuilder();
    getQueries().stream()
        .map(GetQuery::getEntries)
        .forEach(uriBuilder::addParameters);
    return uriBuilder.toString();
  }
}
