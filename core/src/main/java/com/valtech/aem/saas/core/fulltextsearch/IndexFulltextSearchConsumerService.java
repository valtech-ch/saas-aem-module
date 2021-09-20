package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConsumerService;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchResults;
import com.valtech.aem.saas.api.fulltextsearch.Result;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.response.FallbackHighlighting;
import com.valtech.aem.saas.core.http.response.Highlighting;
import com.valtech.aem.saas.core.http.response.HighlightingDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.ResponseBody;
import com.valtech.aem.saas.core.http.response.ResponseBodyDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.ResponseHeaderDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.http.response.SearchResult;
import com.valtech.aem.saas.core.http.response.SuggestionDataExtractionStrategy;
import com.valtech.aem.saas.core.util.LoggedOptional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Builder(toBuilder = true)
public final class IndexFulltextSearchConsumerService implements FulltextSearchConsumerService {

  private final SearchRequestExecutorService searchRequestExecutorService;
  private final String apiUrl;
  private final boolean enableAutoSuggest;
  private final boolean enableBestBets;

  @Override
  public Optional<FulltextSearchResults> getResults(FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload) {
    validateCommonConfigs();
    String requestUrl = getRequestUrl(fulltextSearchGetRequestPayload);
    log.debug("Search GET Request: {}", requestUrl);
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(new SearchRequestGet(requestUrl));
    if (searchResponse.isPresent()) {
      printResponseHeaderInLog(searchResponse.get());
      return getFulltextSearchResults(searchResponse.get());
    }
    return Optional.empty();
  }

  private String getRequestUrl(FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload) {
    return String.format("%s%s",
        apiUrl,
        fulltextSearchGetRequestPayload.getPayload());
  }

  private Optional<FulltextSearchResults> getFulltextSearchResults(SearchResponse searchResponse) {
    Optional<ResponseBody> responseBody = searchResponse.get(new ResponseBodyDataExtractionStrategy());
    if (responseBody.isPresent()) {
      Highlighting highlighting = searchResponse.get(new HighlightingDataExtractionStrategy())
          .orElse(FallbackHighlighting.getInstance());
      Stream<Result> results = getProcessedResults(responseBody.get().getDocs(), highlighting);
      if (enableBestBets) {
        log.debug("Best bets is enabled. Results will be sorted so that best bet results are on top.");
        results = results.sorted(Comparator.comparing(Result::isBestBet).reversed());
      }
      FulltextSearchResults.FulltextSearchResultsBuilder fulltextSearchResultsBuilder =
          FulltextSearchResults.builder()
              .totalResultsFound(responseBody.get().getNumFound())
              .currentResultPage(responseBody.get().getStart())
              .results(results.collect(Collectors.toList()));
      if (enableAutoSuggest) {
        log.debug("Auto suggest is enabled.");
        searchResponse.get(new SuggestionDataExtractionStrategy()).flatMap(suggestion -> LoggedOptional.of(suggestion,
                logger -> logger.warn("No suggestion has been found in search response")))
            .ifPresent(fulltextSearchResultsBuilder::suggestion);
      }
      return Optional.of(fulltextSearchResultsBuilder.build());
    } else {
      log.error("No response body is found.");
    }
    return Optional.empty();
  }

  private Stream<Result> getProcessedResults(List<SearchResult> searchResults,
      Highlighting highlighting) {
    return searchResults.stream()
        .map(searchResult -> getResult(searchResult, highlighting));
  }

  private Result getResult(SearchResult searchResult, Highlighting highlighting) {
    return Result.builder()
        .url(searchResult.getUrl())
        .title(new HighlightedTitleResolver(searchResult, highlighting).getTitle())
        .description(new HighlightedDescriptionResolver(searchResult, highlighting).getDescription())
        .bestBet(searchResult.isElevated())
        .build();
  }

  private void printResponseHeaderInLog(SearchResponse searchResponse) {
    searchResponse.get(new ResponseHeaderDataExtractionStrategy())
        .ifPresent(header -> log.debug("Response Header: {}", header));
  }

  private void validateCommonConfigs() {
    if (searchRequestExecutorService == null) {
      throw new IllegalStateException("Missing an instance of SearchRequestExecutorService.");
    }
    if (StringUtils.isBlank(apiUrl)) {
      throw new IllegalStateException("Fulltext search api base url must be specified.");
    }
  }

}
