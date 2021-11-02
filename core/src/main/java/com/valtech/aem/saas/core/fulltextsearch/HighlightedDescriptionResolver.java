package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.core.http.response.dto.HighlightingDTO;
import com.valtech.aem.saas.core.http.response.dto.SearchResultDTO;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Helper class for resolving the highlighted description text, for a specified search result and a highlighting flag.
 */
@RequiredArgsConstructor
public final class HighlightedDescriptionResolver {

  private static final String META_DESCRIPTION_PREFIX = "meta_description_";
  private static final String CONTENT_PREFIX = "content_";
  private final SearchResultDTO searchResultDto;
  private final HighlightingDTO highlightingDto;

  /**
   * Returns the result's description, highlighted or not, depending on the available data and highlighting flag's
   * value
   *
   * @return the result's description.
   */
  public String getDescription() {
    if (StringUtils.isBlank(searchResultDto.getId())) {
      return searchResultDto.getMetaDescription();
    }
    return Optional.ofNullable(highlightingDto.getItems().get(searchResultDto.getId()))
        .map(stringListMap -> stringListMap.getOrDefault(META_DESCRIPTION_PREFIX + searchResultDto.getLanguage(),
            stringListMap.get(CONTENT_PREFIX + searchResultDto.getLanguage())))
        .map(list -> String.join(" ", list))
        .filter(StringUtils::isNotBlank)
        .orElse(searchResultDto.getMetaDescription());
  }

}
