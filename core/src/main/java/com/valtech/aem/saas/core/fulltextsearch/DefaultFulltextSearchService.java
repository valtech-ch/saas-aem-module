package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.ResultDTO;
import com.valtech.aem.saas.api.query.FacetsQuery;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FiltersQuery;
import com.valtech.aem.saas.api.query.GetQueryStringConstructor;
import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.PaginationQuery;
import com.valtech.aem.saas.api.query.TermQuery;
import com.valtech.aem.saas.core.fulltextsearch.DefaultFulltextSearchService.Configuration;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.response.FacetFieldsDataExtractionStrategy;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    service = FulltextSearchService.class)
@Designate(ocd = Configuration.class)
public class DefaultFulltextSearchService implements FulltextSearchService {

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  @Reference
  private SearchRequestExecutorService searchRequestExecutorService;

  private Configuration configuration;

  @Override
  public Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
      String searchText,
      @NonNull String language,
      int start,
      int rows,
      Set<Filter> filters,
      Set<String> facets) {
    String requestUrl = getRequestUrl(getApiUrl(searchConfiguration.getIndex()),
        createQueryString(searchText,
            language,
            start,
            rows,
            getEffectiveFilters(searchConfiguration.getFilters(), filters),
            facets));
    log.debug("Search GET Request: {}", requestUrl);
    Optional<SearchResponse> searchResponse = searchRequestExecutorService.execute(new SearchRequestGet(requestUrl));
    if (searchResponse.isPresent()) {
      printResponseHeaderInLog(searchResponse.get());
      return getFulltextSearchResults(searchResponse.get(), searchConfiguration.isAutoSuggestEnabled(),
          searchConfiguration.isBestBetsEnabled());
    }
    return Optional.empty();
  }

  private Set<Filter> getEffectiveFilters(Set<Filter> contextFilters, Set<Filter> specifiedFilters) {
    return Stream.concat(contextFilters.stream(), specifiedFilters.stream()).collect(Collectors.toSet());
  }

  private String createQueryString(String term, String language, int start, int rows, Set<Filter> filters,
      Set<String> facets) {
    return GetQueryStringConstructor.builder()
        .query(new TermQuery(term))
        .query(new LanguageQuery(language))
        .query(new PaginationQuery(start, rows))
        .query(FiltersQuery.builder().filters(CollectionUtils.emptyIfNull(filters)).build())
        .query(FacetsQuery.builder().fields(CollectionUtils.emptyIfNull(facets)).build())
        .build()
        .getQueryString();
  }

  private String getRequestUrl(String apiUrl, String queryString) {
    return String.format("%s%s", apiUrl, queryString);
  }

  private Optional<FulltextSearchResultsDTO> getFulltextSearchResults(SearchResponse searchResponse,
      boolean enableAutoSuggest,
      boolean enableBestBets) {
    Optional<ResponseBodyDTO> responseBody = searchResponse.get(new ResponseBodyDataExtractionStrategy());
    if (responseBody.isPresent()) {
      HighlightingDTO highlightingDto = searchResponse.get(new HighlightingDataExtractionStrategy())
          .filter(h -> h.getItems() != null)
          .orElse(FallbackHighlightingDTO.getInstance());
      Stream<ResultDTO> results = getProcessedResults(responseBody.get().getDocs(), highlightingDto);
      if (enableBestBets) {
        log.debug("Best bets is enabled. Results will be sorted so that best bet results are on top.");
        results = results.sorted(Comparator.comparing(ResultDTO::isBestBet).reversed());
      }
      FulltextSearchResultsDTO.FulltextSearchResultsDTOBuilder fulltextSearchResultsBuilder =
          FulltextSearchResultsDTO.builder()
              .totalResultsFound(responseBody.get().getNumFound())
              .currentResultPage(responseBody.get().getStart())
              .results(results.collect(Collectors.toList()))
              .facetFieldsResults(
                  searchResponse.get(new FacetFieldsDataExtractionStrategy()).orElse(Collections.emptyList()));
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

  private Stream<ResultDTO> getProcessedResults(List<SearchResultDTO> searchResultDtos,
      HighlightingDTO highlightingDto) {
    return searchResultDtos.stream()
        .map(searchResult -> getResult(searchResult, highlightingDto));
  }

  private ResultDTO getResult(SearchResultDTO searchResultDto, HighlightingDTO highlightingDto) {
    return ResultDTO.builder()
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

  }
}
