package com.valtech.aemsaas.core.utils.search;

import com.valtech.aemsaas.core.models.search.FulltextSearchGetQuery;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

public abstract class GetQueryStringConstructor {

  protected abstract List<FulltextSearchGetQuery> getQueries();

  public String getQueryString() {
    return getQueries().stream()
        .map(FulltextSearchGetQuery::getString)
        .filter(StringUtils::isNotBlank)
        .collect(
            Collectors.joining(FulltextSearchGetQuery.DELIMITER, FulltextSearchGetQuery.PREFIX, StringUtils.EMPTY));
  }
}
