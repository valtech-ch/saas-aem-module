package com.valtech.aemsaas.core.utils.search.results;

import com.valtech.aemsaas.core.models.response.search.Highlighting;
import com.valtech.aemsaas.core.models.response.search.SearchResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public final class HighlightedDescriptionResolver {

  private static final String META_DESCRIPTION_PREFIX = "meta_description_";
  private static final String CONTENT_PREFIX = "content_";
  private final SearchResult searchResult;
  private final Highlighting highlighting;

  public String getDescription() {
    if (StringUtils.isBlank(searchResult.getId())) {
      return searchResult.getMetaDescription();
    }
    return Optional.ofNullable(highlighting.getItems().get(searchResult.getId()))
        .map(stringListMap -> stringListMap.getOrDefault(META_DESCRIPTION_PREFIX + searchResult.getLanguage(),
            stringListMap.get(CONTENT_PREFIX + searchResult.getLanguage())))
        .map(list -> String.join(" ", list))
        .filter(StringUtils::isNotBlank)
        .orElse(searchResult.getMetaDescription());
  }

}
