package com.valtech.aem.saas.core.fulltextsearch;

import java.util.List;
import lombok.Builder;
import lombok.Value;

/**
 * Pojo for serialization of search cmp configuration.
 */
@Value
@Builder
public class SearchConfigurationJson {

  String title;
  String searchFieldPlaceholderText;
  String searchButtonText;
  String loadMoreButtonText;
  int autocompleteTriggerThreshold;
  List<String> searchTabs;
}
