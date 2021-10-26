package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchPayloadDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.query.GetQueryStringConstructor;
import com.valtech.aem.saas.api.query.Query;
import com.valtech.aem.saas.core.common.saas.SaasIndexValidator;
import com.valtech.aem.saas.core.fulltextsearch.DefaultFulltextSearchService.Configuration;
import com.valtech.aem.saas.core.fulltextsearch.dto.DefaultFulltextSearchResultsDTO;
import com.valtech.aem.saas.core.fulltextsearch.dto.DefaultResultDTO;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.response.HighlightingDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.ResponseBodyDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.ResponseHeaderDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.http.response.SuggestionDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.dto.FallbackHighlightingDTO;
import com.valtech.aem.saas.core.http.response.dto.HighlightingDTO;
import com.valtech.aem.saas.core.http.response.dto.ResponseBodyDTO;
import com.valtech.aem.saas.core.http.response.dto.SearchResultDTO;
import com.valtech.aem.saas.core.util.LoggedOptional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
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
public class DefaultFulltextSearchService implements
    FulltextSearchService,
    FulltextSearchConfigurationService {

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
  public Optional<FulltextSearchResultsDTO> getResults(@NonNull String index,
      @NonNull FulltextSearchPayloadDTO fulltextSearchPayloadDto,
      boolean enableAutoSuggest,
      boolean enableBestBets) {
    SaasIndexValidator.getInstance().validate(index);
    String requestUrl = getRequestUrl(index, fulltextSearchPayloadDto);
    log.debug("Search GET Request: {}", requestUrl);
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(new SearchRequestGet(requestUrl));
    if (searchResponse.isPresent()) {
      printResponseHeaderInLog(searchResponse.get());
      return getFulltextSearchResults(searchResponse.get(), enableAutoSuggest, enableBestBets);
    }
    return Optional.empty();
  }

  private String createQueryString(FulltextSearchPayloadDTO fulltextSearchPayloadDto) {
    if (validate(fulltextSearchPayloadDto)) {
      return GetQueryStringConstructor.builder()
          .query(fulltextSearchPayloadDto.getTermQuery())
          .query(fulltextSearchPayloadDto.getLanguageQuery())
          .queries(fulltextSearchPayloadDto.getOptionalQueries())
          .build()
          .getQueryString();
    }
    throw new IllegalStateException("Payload is not valid.");
  }

  private boolean validate(FulltextSearchPayloadDTO fulltextSearchPayloadDto) {
    return isNotEmpty(fulltextSearchPayloadDto.getTermQuery()) && isNotEmpty(
        fulltextSearchPayloadDto.getLanguageQuery());
  }

  private boolean isNotEmpty(Query query) {
    return Optional.ofNullable(query)
        .map(Query::getEntries)
        .filter(CollectionUtils::isNotEmpty)
        .isPresent();
  }


  private String getRequestUrl(String index, FulltextSearchPayloadDTO fulltextSearchPayloadDto) {
    return String.format("%s%s",
        getApiUrl(index),
        createQueryString(fulltextSearchPayloadDto));
  }

  private Optional<FulltextSearchResultsDTO> getFulltextSearchResults(SearchResponse searchResponse,
      boolean enableAutoSuggest,
      boolean enableBestBets) {
    Optional<ResponseBodyDTO> responseBody = searchResponse.get(new ResponseBodyDataExtractionStrategy());
    if (responseBody.isPresent()) {
      HighlightingDTO highlightingDto = searchResponse.get(new HighlightingDataExtractionStrategy())
          .orElse(FallbackHighlightingDTO.getInstance());
      Stream<DefaultResultDTO> results = getProcessedResults(responseBody.get().getDocs(), highlightingDto);
      if (enableBestBets) {
        log.debug("Best bets is enabled. Results will be sorted so that best bet results are on top.");
        results = results.sorted(Comparator.comparing(DefaultResultDTO::isBestBet).reversed());
      }
      DefaultFulltextSearchResultsDTO.DefaultFulltextSearchResultsDTOBuilder fulltextSearchResultsBuilder =
          DefaultFulltextSearchResultsDTO.builder()
              .totalResultsFound(responseBody.get().getNumFound())
              .currentResultPage(responseBody.get().getStart())
              .results(results.collect(Collectors.toList()));
      if (enableAutoSuggest) {
        log.debug("Auto suggest is enabled.");
        searchResponse.get(new SuggestionDataExtractionStrategy()).flatMap(suggestion -> LoggedOptional.of(suggestion,
                logger -> logger.debug("No suggestion has been found in search response")))
            .ifPresent(fulltextSearchResultsBuilder::suggestion);
      }
      return Optional.of(fulltextSearchResultsBuilder.build());
    } else {
      log.error("No response body is found.");
    }
    return Optional.empty();
  }

  private Stream<DefaultResultDTO> getProcessedResults(List<SearchResultDTO> searchResultDtos,
      HighlightingDTO highlightingDto) {
    return searchResultDtos.stream()
        .map(searchResult -> getResult(searchResult, highlightingDto));
  }

  private DefaultResultDTO getResult(SearchResultDTO searchResultDto, HighlightingDTO highlightingDto) {
    return DefaultResultDTO.builder()
        .url(searchResultDto.getUrl())
        .title(new HighlightedTitleResolver(searchResultDto, highlightingDto).getTitle())
        .description(new HighlightedDescriptionResolver(searchResultDto, highlightingDto).getDescription())
        .bestBet(searchResultDto.isElevated())
        .build();
  }

  private void printResponseHeaderInLog(SearchResponse searchResponse) {
    searchResponse.get(new ResponseHeaderDataExtractionStrategy())
        .ifPresent(header -> log.debug("Response Header: {}", header));
  }

  private String getApiUrl(String index) {
    return String.format("%s%s/%s%s",
        searchServiceConnectionConfigurationService.getBaseUrl(),
        configuration.fulltextSearchService_apiVersion(),
        index,
        configuration.fulltextSearchService_apiAction());
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
    String DEFAULT_API_VERSION_PATH = "/api/v3"; // NOSONAR

    @AttributeDefinition(name = "Api base path",
        description = "Api base path",
        type = AttributeType.STRING)
    String fulltextSearchService_apiVersion() default DEFAULT_API_VERSION_PATH; // NOSONAR

    @AttributeDefinition(name = "Api action",
        description = "What kind of action should be defined",
        type = AttributeType.STRING)
    String fulltextSearchService_apiAction() default DEFAULT_API_ACTION; // NOSONAR

    @AttributeDefinition(name = "Rows max limit.",
        description = "Maximum number of results per page allowed.",
        type = AttributeType.INTEGER)
    int fulltextSearchService_rowsMaxLimit() default DEFAULT_ROWS_MAX_LIMIT; // NOSONAR

  }
}
