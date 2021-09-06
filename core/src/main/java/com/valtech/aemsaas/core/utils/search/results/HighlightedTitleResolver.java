package com.valtech.aemsaas.core.utils.search.results;

import com.day.cq.commons.LanguageUtil;
import com.valtech.aemsaas.core.models.response.search.Highlighting;
import com.valtech.aemsaas.core.models.response.search.SearchResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
public final class HighlightedTitleResolver {

  private static final String TITLE_PREFIX = "title_";
  private static final String HIGHLIGHTED_ITEMS_DELIMITER = " ";
  private final SearchResult searchResult;
  private final Highlighting highlighting;

  public String getTitle() {
    if (StringUtils.isBlank(searchResult.getId())) {
      return searchResult.getTitle();
    }
    return Optional.ofNullable(highlighting.getItems().get(searchResult.getId()))
        .map(stringListMap -> stringListMap.get(TITLE_PREFIX + LanguageUtil.getLanguage(searchResult.getLanguage())
            .getLanguageCode()))
        .map(list -> String.join(HIGHLIGHTED_ITEMS_DELIMITER, list))
        .filter(StringUtils::isNotBlank)
        .orElse(searchResult.getTitle());
  }

}
