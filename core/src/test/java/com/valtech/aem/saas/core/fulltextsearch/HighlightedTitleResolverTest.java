package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import com.valtech.aem.saas.core.http.response.dto.HighlightingDTO;
import com.valtech.aem.saas.core.http.response.dto.SearchResultDTO;
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
  SearchResultDTO searchResultDto;

  @Mock
  HighlightingDTO highlightingDto;

  @BeforeEach
  void setUp() {
    when(searchResultDto.getTitle()).thenReturn(SEARCH_RESULT_TITLE);
  }

  @Test
  void getTitle_noHighlightEntryAvailable() {
    setupInput_noHighlightEntryAvailable();
    assertThat(new HighlightedTitleResolver(searchResultDto, highlightingDto).getTitle(), is(SEARCH_RESULT_TITLE));
  }

  @Test
  void getTitle_noResultIdAvailable() {
    setupInput_noResultIdAvailable();
    assertThat(new HighlightedTitleResolver(searchResultDto, highlightingDto).getTitle(), is(SEARCH_RESULT_TITLE));
  }

  @Test
  void getTitle_titleInHighlightingEntryAvailable() {
    when(searchResultDto.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_titleInHighlightingEntryAvailable();
    assertThat(new HighlightedTitleResolver(searchResultDto, highlightingDto).getTitle(), is(HIGHLIGHTED_TITLE));
  }

  @Test
  void getTitle_noTitleInHighlightingEntryAvailable() {
    when(searchResultDto.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_noTitleInHighlightingEntryAvailable();
    assertThat(new HighlightedTitleResolver(searchResultDto, highlightingDto).getTitle(), is(SEARCH_RESULT_TITLE));
  }

  private void setupInput_noHighlightEntryAvailable() {
    when(searchResultDto.getId()).thenReturn(SEARCH_RESULT_ID);
    when(highlightingDto.getItems()).thenReturn(Collections.emptyMap());
  }

  private void setupInput_noResultIdAvailable() {
    when(searchResultDto.getId()).thenReturn(StringUtils.EMPTY);
  }

  private void setupInput_noTitleInHighlightingEntryAvailable() {
    when(searchResultDto.getId()).thenReturn(SEARCH_RESULT_ID);
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("title_en", Collections.emptyList());
    when(highlightingDto.getItems()).thenReturn(Collections.singletonMap(SEARCH_RESULT_ID, highlightingEntry));
  }

  private void setupInput_titleInHighlightingEntryAvailable() {
    when(searchResultDto.getId()).thenReturn(SEARCH_RESULT_ID);
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("title_en", Collections.singletonList(HIGHLIGHTED_TITLE));
    when(highlightingDto.getItems()).thenReturn(Collections.singletonMap(SEARCH_RESULT_ID, highlightingEntry));
  }
}
