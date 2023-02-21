package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchPingService;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.ResultDTO;
import com.valtech.aem.saas.api.query.FacetsQuery;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FiltersQuery;
import com.valtech.aem.saas.api.query.GetQueryStringConstructor;
import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.PaginationQuery;
import com.valtech.aem.saas.api.query.SearchTemplateQuery;
import com.valtech.aem.saas.api.query.Sort;
import com.valtech.aem.saas.api.query.SortQuery;
import com.valtech.aem.saas.api.query.TermQuery;
import com.valtech.aem.saas.core.fulltextsearch.DefaultFulltextSearchService.Configuration;
import com.valtech.aem.saas.core.http.client.SearchApiRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.request.SearchRequestHead;
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
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Slf4j
@Component(service = {FulltextSearchService.class, FulltextSearchPingService.class})
@ServiceDescription("Search as a Service - Fulltext Search Service")
@Designate(ocd = Configuration.class)
public class DefaultFulltextSearchService implements FulltextSearchService, FulltextSearchPingService {

    @Reference
    private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

    @Reference
    private SearchApiRequestExecutorService searchApiRequestExecutorService;

    private Configuration configuration;

    @Override
    public Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
        String searchText,
        @NonNull String language,
        int start,
        int rows,
        Set<Filter> filters,
        Set<String> facets,
        String template) {
        return getResults(searchConfiguration, searchText, language, start, rows, filters, facets, false, template, null);
    }
    @Override
    public Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
                                                         String searchText,
                                                         @NonNull String language,
                                                         int start,
                                                         int rows,
                                                         Set<Filter> filters,
                                                         Set<String> facets,
                                                         boolean disableContextFilters,
                                                         String template) {
        return getResults(searchConfiguration, searchText, language, start, rows, filters, facets, disableContextFilters, template, null);
    }

    @Override
    public Optional<FulltextSearchResultsDTO> getResults(@NonNull SearchCAConfigurationModel searchConfiguration,
        String searchText,
        @NonNull String language,
        int start,
        int rows,
        Set<Filter> filters,
        Set<String> facets,
        boolean disableContextFilters,
        String template,
        List<Pair<String, Sort>> sortParameters) {
        String requestUrl = getRequestUrl(getApiUrl(searchConfiguration.getIndex()),
            createQueryString(searchText,
                language,
                start,
                rows,
                getEffectiveFilters(searchConfiguration.getFilters(),
                    filters, disableContextFilters),
                facets,
                template,
                sortParameters));
        log.debug("Search GET Request: {}", requestUrl);
        Optional<SearchResponse> searchResponse =
            searchApiRequestExecutorService.execute(new SearchRequestGet(requestUrl));
        if (searchResponse.isPresent()) {
            printResponseHeaderInLog(searchResponse.get());
            return getFulltextSearchResults(searchResponse.get(),
                searchConfiguration.isAutoSuggestEnabled(),
                searchConfiguration.isBestBetsEnabled());
        }
        return Optional.empty();
    }

    @Override
    public boolean ping(@NonNull SearchCAConfigurationModel searchConfiguration) {
        String url = getApiUrl(searchConfiguration.getIndex());
        SearchRequest pingRequest = new SearchRequestHead(url);
        return searchApiRequestExecutorService.execute(pingRequest).map(SearchResponse::isSuccess).orElse(false);
    }

    private Set<Filter> getEffectiveFilters(Set<Filter> contextFilters, Set<Filter> specifiedFilters, boolean disableContextFilters) {
        Set<Filter> filters = new HashSet<>();
        if (!disableContextFilters) {
            Optional.ofNullable(contextFilters).ifPresent(filters::addAll);
        }
        Optional.ofNullable(specifiedFilters).ifPresent(filters::addAll);
        return filters;
    }


    private String createQueryString(String term, //NOSONAR
                                     String language,
                                     int start,
                                     int rows,
                                     Set<Filter> filters,
                                     Set<String> facets,
                                     String template,
                                     List<Pair<String, Sort>> sortParameters) {
        GetQueryStringConstructor.GetQueryStringConstructorBuilder builder =
                GetQueryStringConstructor.builder()
                                         .query(new TermQuery(term))
                                         .query(new LanguageQuery(language))
                                         .query(new PaginationQuery(start,
                                                                    rows))
                                         .query(FiltersQuery.builder()
                                                            .filters(CollectionUtils.emptyIfNull(filters))
                                                            .build())
                                         .query(FacetsQuery.builder()
                                                           .fields(CollectionUtils.emptyIfNull(facets))
                                                           .build());
        if (StringUtils.isNotBlank(template)) {
            builder.query(new SearchTemplateQuery(template));
        }
        if (CollectionUtils.isNotEmpty(sortParameters)) {
            builder.query(new SortQuery(sortParameters));
        }
        return builder.build().getQueryString();
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
                                            .facetFieldsResults(searchResponse.get(new FacetFieldsDataExtractionStrategy())
                                                                              .orElse(Collections.emptyList()));
            if (enableAutoSuggest) {
                log.debug("Auto suggest is enabled.");
                searchResponse.get(new SuggestionDataExtractionStrategy())
                              .flatMap(suggestion -> LoggedOptional.of(suggestion,
                                                                       logger -> logger.debug(
                                                                               "No suggestion has been found in " +
                                                                                       "search response")))
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
        return searchResultDtos.stream().map(searchResult -> getResult(searchResult, highlightingDto));
    }

    private ResultDTO getResult(SearchResultDTO searchResultDto, HighlightingDTO highlightingDto) {
        return ResultDTO.builder()
                        .url(searchResultDto.getUrl())
                        .title(new HighlightedTitleResolver(searchResultDto, highlightingDto).getTitle())
                        .description(new HighlightedDescriptionResolver(searchResultDto,
                                                                        highlightingDto).getDescription())
                        .bestBet(searchResultDto.isElevated())
                        .repositoryPath(searchResultDto.getRepositoryPathUrl())
                        .build();
    }

    private void printResponseHeaderInLog(SearchResponse searchResponse) {
        searchResponse.get(new ResponseHeaderDataExtractionStrategy())
                      .ifPresent(header -> log.debug("Response Header: {}", header));
    }

    private String getApiUrl(String index) {
        return String.format("%s%s/%s%s",
                             searchApiRequestExecutorService.getBaseUrl(),
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

        @AttributeDefinition(name = "Api version path",
                             description = "Api version path",
                             type = AttributeType.STRING) String fulltextSearchService_apiVersion() default DEFAULT_API_VERSION_PATH; // NOSONAR

        @AttributeDefinition(name = "Api action",
                             description = "What kind of action should be defined",
                             type = AttributeType.STRING) String fulltextSearchService_apiAction() default DEFAULT_API_ACTION; // NOSONAR

    }
}
