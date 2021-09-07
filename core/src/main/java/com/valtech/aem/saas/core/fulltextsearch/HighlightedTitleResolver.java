package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.core.http.response.Highlighting;
import com.valtech.aem.saas.core.http.response.SearchResult;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper class for resolving the highlighted title text, for a specified search result and a highlighting flag.
 */
@RequiredArgsConstructor
public final class HighlightedTitleResolver {

  private static final String TITLE_PREFIX = "title_";
  private static final String HIGHLIGHTED_ITEMS_DELIMITER = " ";
  private final SearchResult searchResult;
  private final Highlighting highlighting;

  /**
   * Returns the result's description, highlighted or not, depending on the available data and highlighting flag's
   * value
   *
   * @return the result's description.
   */
  public String getTitle() {
    if (StringUtils.isBlank(searchResult.getId())) {
      return searchResult.getTitle();
    }
    return Optional.ofNullable(highlighting.getItems().get(searchResult.getId()))
        .map(stringListMap -> stringListMap.get(TITLE_PREFIX + searchResult.getLanguage()))
        .map(list -> String.join(HIGHLIGHTED_ITEMS_DELIMITER, list))
        .filter(StringUtils::isNotBlank)
        .orElse(searchResult.getTitle());
  }

}
