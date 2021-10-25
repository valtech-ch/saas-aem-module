package com.valtech.aem.saas.core.fulltextsearch;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import com.valtech.aem.saas.core.http.response.dto.HighlightingDto;
import com.valtech.aem.saas.core.http.response.dto.SearchResultDto;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighlightedDescriptionResolverTest {

  private static final String SEARCH_RESULT_ID = "foo";
  private static final String SEARCH_RESULT_META_DESCRIPTION = "Valtech Group organization chart";
  private static final String SEARCH_RESULT_LANGUAGE = "en";
  private static final String HIGHLIGHTED_META_DESCRIPTION = "<em>Valtech</em> Group organization chart";
  private static final String HIGHLIGHTED_CONTENT = "<em>Valtech</em> <em>Valtech</em> Group Luxembourg (HQ) <em>Valtech</em> SE UK <em>Valtech</em> Ltd. 100% True Clarity* * 100% El Chalten 100% <em>Valtech</em> LLC. (Ukraine) 100% Argentina <em>Valtech</em> Digital SA 95% Brazil <em>Valtech</em> Brasil";

  @Mock
  SearchResultDto searchResultDto;

  @Mock
  HighlightingDto highlightingDto;

  @Test
  void getMetaDescription_noHighlightEntryAvailable() {
    setupInput_noHighlightEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResultDto, highlightingDto).getDescription(),
        is(SEARCH_RESULT_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_noResultIdAvailable() {
    setupInput_noResultIdAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResultDto, highlightingDto).getDescription(),
        is(SEARCH_RESULT_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_metaDescriptionInHighlightingEntryAvailable() {
    when(searchResultDto.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_metaDescriptionInHighlightingEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResultDto, highlightingDto).getDescription(),
        is(HIGHLIGHTED_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_noMetaDescriptionInHighlightingEntryAvailable() {
    when(searchResultDto.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResultDto.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_noMetaDescriptionInHighlightingEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResultDto, highlightingDto).getDescription(),
        is(SEARCH_RESULT_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_contentInHighlightingEntryAvailable() {
    when(searchResultDto.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResultDto.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_contentInHighlightingEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResultDto, highlightingDto).getDescription(),
        is(HIGHLIGHTED_CONTENT));
  }

  private void setupInput_noHighlightEntryAvailable() {
    when(searchResultDto.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResultDto.getId()).thenReturn(SEARCH_RESULT_ID);
    when(highlightingDto.getItems()).thenReturn(Collections.emptyMap());
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("meta_description_en", Collections.singletonList(HIGHLIGHTED_META_DESCRIPTION));
  }

  private void setupInput_noResultIdAvailable() {
    when(searchResultDto.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResultDto.getId()).thenReturn(StringUtils.EMPTY);
  }

  private void setupInput_noMetaDescriptionInHighlightingEntryAvailable() {
    when(searchResultDto.getId()).thenReturn(SEARCH_RESULT_ID);
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("meta_description_en", Collections.emptyList());
    when(highlightingDto.getItems()).thenReturn(Collections.singletonMap(SEARCH_RESULT_ID, highlightingEntry));
  }

  private void setupInput_contentInHighlightingEntryAvailable() {
    when(searchResultDto.getId()).thenReturn(SEARCH_RESULT_ID);
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("content_en", Collections.singletonList(HIGHLIGHTED_CONTENT));
    when(highlightingDto.getItems()).thenReturn(Collections.singletonMap(SEARCH_RESULT_ID, highlightingEntry));
  }

  private void setupInput_metaDescriptionInHighlightingEntryAvailable() {
    when(searchResultDto.getId()).thenReturn(SEARCH_RESULT_ID);
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("meta_description_en", Collections.singletonList(HIGHLIGHTED_META_DESCRIPTION));
    when(highlightingDto.getItems()).thenReturn(Collections.singletonMap(SEARCH_RESULT_ID, highlightingEntry));
  }
}
