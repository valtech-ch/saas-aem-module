package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.core.http.response.dto.HighlightingDTO;
import com.valtech.aem.saas.core.http.response.dto.SearchResultDTO;
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
  private final SearchResultDTO searchResultDto;
  private final HighlightingDTO highlightingDto;

  /**
   * Returns the result's description, highlighted or not, depending on the available data and highlighting flag's
   * value
   *
   * @return the result's description.
   */
  public String getTitle() {
    if (StringUtils.isBlank(searchResultDto.getId())) {
      return searchResultDto.getTitle();
    }
    return Optional.ofNullable(highlightingDto.getItems().get(searchResultDto.getId()))
        .map(stringListMap -> stringListMap.get(TITLE_PREFIX + searchResultDto.getLanguage()))
        .map(list -> String.join(HIGHLIGHTED_ITEMS_DELIMITER, list))
        .filter(StringUtils::isNotBlank)
        .orElse(searchResultDto.getTitle());
  }

}
