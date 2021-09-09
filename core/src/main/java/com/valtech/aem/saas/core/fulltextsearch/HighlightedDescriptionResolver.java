package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.core.http.response.Highlighting;
import com.valtech.aem.saas.core.http.response.SearchResult;
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
  private final SearchResult searchResult;
  private final Highlighting highlighting;

  /**
   * Returns the result's description, highlighted or not, depending on the available data and highlighting flag's
   * value
   *
   * @return the result's description.
   */
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
