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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class HighlightedDescriptionResolverTest {

  private static final String SEARCH_RESULT_ID = "foo";
  private static final String SEARCH_RESULT_META_DESCRIPTION = "SIX understands clients’ challenges and monitors what is happening in other industries. This allows us to drive the transformation of financial markets.";
  private static final String SEARCH_RESULT_LANGUAGE = "en";
  private static final String HIGHLIGHTED_META_DESCRIPTION = "<em>SIX</em> understands clients’ challenges and monitors what is happening in other industries. This allows us to drive the transformation of financial markets.";
  private static final String HIGHLIGHTED_CONTENT = "How <em>SIX</em> Innovates How Innovation Gets Done For <em>SIX</em>, innovation is not just a buzzword, it is an elementary component of our corporate culture. We not only operate the infrastructure of the Swiss";

  @Mock
  SearchResult searchResult;

  Map<String, Map<String, List<String>>> highlighting;

  @Test
  void getMetaDescription_noHighlightEntryAvailable() {
    setupInput_noHighlightEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResult, highlighting).getDescription(),
        is(SEARCH_RESULT_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_noResultIdAvailable() {
    setupInput_noResultIdAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResult, highlighting).getDescription(),
        is(SEARCH_RESULT_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_metaDescriptionInHighlightingEntryAvailable() {
    when(searchResult.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_metaDescriptionInHighlightingEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResult, highlighting).getDescription(),
        is(HIGHLIGHTED_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_noMetaDescriptionInHighlightingEntryAvailable() {
    when(searchResult.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResult.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_noMetaDescriptionInHighlightingEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResult, highlighting).getDescription(),
        is(SEARCH_RESULT_META_DESCRIPTION));
  }

  @Test
  void getMetaDescription_contentInHighlightingEntryAvailable() {
    when(searchResult.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResult.getLanguage()).thenReturn(SEARCH_RESULT_LANGUAGE);
    setupInput_contentInHighlightingEntryAvailable();
    assertThat(new HighlightedDescriptionResolver(searchResult, highlighting).getDescription(),
        is(HIGHLIGHTED_CONTENT));
  }

  private void setupInput_noHighlightEntryAvailable() {
    when(searchResult.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResult.getId()).thenReturn(SEARCH_RESULT_ID);
    highlighting = new HashMap<>();
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("meta_description_en", Collections.singletonList(HIGHLIGHTED_META_DESCRIPTION));
  }

  private void setupInput_noResultIdAvailable() {
    when(searchResult.getMetaDescription()).thenReturn(SEARCH_RESULT_META_DESCRIPTION);
    when(searchResult.getId()).thenReturn(StringUtils.EMPTY);
  }

  private void setupInput_noMetaDescriptionInHighlightingEntryAvailable() {
    when(searchResult.getId()).thenReturn(SEARCH_RESULT_ID);
    highlighting = new HashMap<>();
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("meta_description_en", Collections.emptyList());
    highlighting.put(SEARCH_RESULT_ID, highlightingEntry);
  }

  private void setupInput_contentInHighlightingEntryAvailable() {
    when(searchResult.getId()).thenReturn(SEARCH_RESULT_ID);
    highlighting = new HashMap<>();
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("content_en", Collections.singletonList(HIGHLIGHTED_CONTENT));
    highlighting.put(SEARCH_RESULT_ID, highlightingEntry);
  }

  private void setupInput_metaDescriptionInHighlightingEntryAvailable() {
    when(searchResult.getId()).thenReturn(SEARCH_RESULT_ID);
    highlighting = new HashMap<>();
    Map<String, List<String>> highlightingEntry = new HashMap<>();
    highlightingEntry.put("meta_description_en", Collections.singletonList(HIGHLIGHTED_META_DESCRIPTION));
    highlighting.put(SEARCH_RESULT_ID, highlightingEntry);
  }
}
