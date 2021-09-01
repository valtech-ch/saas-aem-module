package com.valtech.aemsaas.core.utils.search.results;

import com.valtech.aemsaas.core.models.response.search.SearchResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public final class HighlightedDescriptionResolver {

  private final SearchResult searchResult;
  private final Map<String, Map<String, List<String>>> highlighting;

  public String getDescription() {
    if (StringUtils.isBlank(searchResult.getId())) {
      return searchResult.getMetaDescription();
    }
    return Optional.ofNullable(highlighting.get(searchResult.getId()))
        .map(stringListMap -> stringListMap.getOrDefault("meta_description_" + searchResult.getLanguage(),
            stringListMap.get("content_" + searchResult.getLanguage())))
        .map(list -> String.join(" ", list))
        .filter(StringUtils::isNotBlank)
        .orElse(searchResult.getMetaDescription());
  }

}
