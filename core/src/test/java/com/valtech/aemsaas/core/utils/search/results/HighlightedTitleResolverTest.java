package com.valtech.aemsaas.core.utils.search.results;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import com.valtech.aemsaas.core.models.response.search.SearchResult;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighlightedTitleResolverTest {

  private static final String SEARCH_RESULT_ID = "foo";
  private static final String SEARCH_RESULT_TITLE = "bar";
  private static final String SEARCH_RESULT_LANGUAGE = "en";
  private static final String HIGHLIGHTED_TITLE = "foo <em>bar</em> baz";

  @Mock
  SearchResult searchResult;

  Map<String, Map<String, List<String>>> highlighting;

  @BeforeEach
  void setUp() {
    when(searchResult.getTitle()).thenReturn(SEARCH_RESULT_TITLE);
  }

  @Test
  void getTitle_noHighlightEntryAvailable() {
    setupInput_noHighlightEntryAvailable();
    assertThat(new HighlightedTitleResolver(searchResult, highlighting).getTitle(), is(SEARCH_RESULT_TITLE));
  }

  @Test
  void getTitle_noResultIdAvailable() {
    setupInput_noResultIdAvailable();
    assertThat(new HighlightedTitleResolver(searchResult, highlighting).getTitle(), is(SEARCH_RESULT_TITLE));
  }

  @Test
  void getTitle_titleInHighlightingEntryAvailable() {
    when(searchResult.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_titleInHighlightingEntryAvailable();
    assertThat(new HighlightedTitleResolver(searchResult, highlighting).getTitle(), is(HIGHLIGHTED_TITLE));
  }

  @Test
  void getTitle_noTitleInHighlightingEntryAvailable() {
    when(searchResult.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_noTitleInHighlightingEntryAvailable();
    assertThat(new HighlightedTitleResolver(searchResult, highlighting).getTitle(), is(SEARCH_RESULT_TITLE));
  }

  private void setupInput_noHighlightEntryAvailable() {
    when(searchResult.getId()).thenReturn(SEARCH_RESULT_ID);
    highlighting = new HashMap<>();
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("title_en", Collections.singletonList(HIGHLIGHTED_TITLE));
  }

  private void setupInput_noResultIdAvailable() {
    when(searchResult.getId()).thenReturn(StringUtils.EMPTY);
  }

  private void setupInput_noTitleInHighlightingEntryAvailable() {
    when(searchResult.getId()).thenReturn(SEARCH_RESULT_ID);
    highlighting = new HashMap<>();
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("title_en", Collections.emptyList());
    highlighting.put(SEARCH_RESULT_ID, highlightingEntry);
  }

  private void setupInput_titleInHighlightingEntryAvailable() {
    when(searchResult.getId()).thenReturn(SEARCH_RESULT_ID);
    highlighting = new HashMap<>();
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("title_en", Collections.singletonList(HIGHLIGHTED_TITLE));
    highlighting.put(SEARCH_RESULT_ID, highlightingEntry);
  }
}
