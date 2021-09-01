package com.valtech.aemsaas.core.utils.search.results;

import com.valtech.aemsaas.core.models.response.search.SearchResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public final class HighlightedTitleResolver {

  private final SearchResult searchResult;
  private final Map<String, Map<String, List<String>>> highlighting;

  public String getTitle() {
    if (StringUtils.isBlank(searchResult.getId())) {
      return searchResult.getTitle();
    }
    return Optional.ofNullable(highlighting.get(searchResult.getId()))
        .map(stringListMap -> stringListMap.get("title_" + searchResult.getLanguage()))
        .map(list -> String.join(" ", list))
        .filter(StringUtils::isNotBlank)
        .orElse(searchResult.getTitle());
  }

}
