package com.valtech.aemsaas.core.services.search.impl;

import com.valtech.aemsaas.core.models.request.SearchRequestGet;
import com.valtech.aemsaas.core.models.response.parse.HighlightingDataExtractionStrategy;
import com.valtech.aemsaas.core.models.response.parse.ResponseBodyDataExtractionStrategy;
import com.valtech.aemsaas.core.models.response.parse.ResponseHeaderDataExtractionStrategy;
import com.valtech.aemsaas.core.models.response.search.FallbackHighlighting;
import com.valtech.aemsaas.core.models.response.search.Highlighting;
import com.valtech.aemsaas.core.models.response.search.ResponseBody;
import com.valtech.aemsaas.core.models.response.search.SearchResponse;
import com.valtech.aemsaas.core.models.response.search.SearchResult;
import com.valtech.aemsaas.core.models.search.payload.FulltextSearchGetRequestPayload;
import com.valtech.aemsaas.core.models.search.results.FulltextSearchResults;
import com.valtech.aemsaas.core.models.search.results.Result;
import com.valtech.aemsaas.core.services.search.FulltextSearchConfigurationService;
import com.valtech.aemsaas.core.services.search.FulltextSearchService;
import com.valtech.aemsaas.core.services.search.SearchRequestExecutorService;
import com.valtech.aemsaas.core.services.search.SearchServiceConnectionConfigurationService;
import com.valtech.aemsaas.core.services.search.impl.DefaultFulltextSearchService.Configuration;
import com.valtech.aemsaas.core.utils.search.results.HighlightedDescriptionResolver;
import com.valtech.aemsaas.core.utils.search.results.HighlightedTitleResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Slf4j
@Component(name = "Search as a Service - Fulltext Search Service",
    service = {FulltextSearchService.class, FulltextSearchConfigurationService.class})
@Designate(ocd = Configuration.class)
public class DefaultFulltextSearchService implements FulltextSearchService, FulltextSearchConfigurationService {

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  @Reference
  private SearchRequestExecutorService searchRequestExecutorService;

  private Configuration configuration;

  @Override
  public int getRowsMaxLimit() {
    return configuration.fulltextSearchService_rowsMaxLimit();
  }

  @Override
  public Optional<FulltextSearchResults> getResults(String index,
      FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload) {
    validateIndex(index);
    String requestUrl = getRequestUrl(index, fulltextSearchGetRequestPayload);
    log.debug("Search GET Request: {}", requestUrl);
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(new SearchRequestGet(requestUrl));
    if (searchResponse.isPresent()) {
      printResponseHeaderInLog(searchResponse.get());
      return getFulltextSearchResults(searchResponse.get());
    }
    return Optional.empty();
  }

  private void validateIndex(String index) {
    if (StringUtils.isBlank(index)) {
      throw new IllegalArgumentException(
          "SaaS index name is missing. Please configure index name in Context Aware configuration.");
    }
  }

  private Optional<FulltextSearchResults> getFulltextSearchResults(SearchResponse searchResponse) {
    Optional<ResponseBody> responseBody = searchResponse.get(new ResponseBodyDataExtractionStrategy());
    if (responseBody.isPresent()) {
      Highlighting highlighting = searchResponse.get(new HighlightingDataExtractionStrategy())
          .orElse(FallbackHighlighting.getInstance());
      return Optional.of(FulltextSearchResults.builder()
          .totalResultsFound(responseBody.get().getNumFound())
          .currentResultPage(responseBody.get().getStart())
          .results(getProcessedResults(responseBody.get().getDocs(), highlighting))
          .build());
    } else {
      log.error("No response body is found.");
    }
    return Optional.empty();
  }

  private void printResponseHeaderInLog(SearchResponse searchResponse) {
    searchResponse.get(new ResponseHeaderDataExtractionStrategy())
        .ifPresent(header -> log.debug("Response Header: {}", header));
  }

  private String getRequestUrl(String index, FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload) {
    String requestUrl = String.format("%s%s%s%s%s",
        searchServiceConnectionConfigurationService.getBaseUrl(),
        configuration.fulltextSearchService_apiBaseUrl(),
        index,
        configuration.fulltextSearchService_apiAction(),
        fulltextSearchGetRequestPayload.getPayload());
    return requestUrl;
  }

  private List<Result> getProcessedResults(List<SearchResult> searchResults,
      Highlighting highlighting) {
    return searchResults.stream()
        .map(searchResult -> getResult(searchResult, highlighting))
        .collect(Collectors.toList());
  }

  private Result getResult(SearchResult searchResult, Highlighting highlighting) {
    return Result.builder()
        .url(searchResult.getUrl())
        .title(new HighlightedTitleResolver(searchResult, highlighting).getTitle())
        .description(new HighlightedDescriptionResolver(searchResult, highlighting).getDescription())
        .build();
  }

  @Activate
  @Modified
  private void activate(Configuration configuration) {
    this.configuration = configuration;
  }

  @ObjectClassDefinition(name = "Search as a Service - Fulltext Search Service Configuration",
      description = "Fulltext Search Api specific details.")
  public @interface Configuration {

    int DEFAULT_ROWS_MAX_LIMIT = 9999;
    String DEFAULT_API_ACTION = "/search";
    String DEFAULT_API_BASE_PATH = "/api/v3";

    @AttributeDefinition(name = "Api base path",
        description = "Api base path",
        type = AttributeType.STRING)
    String fulltextSearchService_apiBaseUrl() default DEFAULT_API_BASE_PATH;

    @AttributeDefinition(name = "Api action",
        description = "What kind of action should be defined",
        type = AttributeType.STRING)
    String fulltextSearchService_apiAction() default DEFAULT_API_ACTION;

    @AttributeDefinition(name = "Rows max limit.",
        description = "Maximum number of results per page allowed.",
        type = AttributeType.INTEGER)
    int fulltextSearchService_rowsMaxLimit() default DEFAULT_ROWS_MAX_LIMIT;

  }
}
