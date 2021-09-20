package com.valtech.aem.saas.core.fulltextsearch;

import static com.valtech.aem.saas.core.fulltextsearch.SearchImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.caconfig.SearchFilterConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchResults;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.Result;
import com.valtech.aem.saas.api.fulltextsearch.Search;
import com.valtech.aem.saas.api.fulltextsearch.SearchResults;
import com.valtech.aem.saas.api.fulltextsearch.Suggestion;
import com.valtech.aem.saas.core.common.request.RequestConsumer;
import com.valtech.aem.saas.core.common.resource.ResourceConsumer;
import com.valtech.aem.saas.core.http.response.Highlighting;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.DefaultTermQuery;
import com.valtech.aem.saas.core.query.FiltersQuery;
import com.valtech.aem.saas.core.query.HighlightingTagQuery;
import com.valtech.aem.saas.core.query.PaginationQuery;
import com.valtech.aem.saas.core.util.StringToInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = {SearchResults.class, ComponentExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchResultsImpl implements SearchResults {

  public static final String RESOURCE_TYPE = "saas-aem-module/components/saas/searchresults";
  public static final String QUERY_PARAM_START = "start";
  public static final int DEFAULT_START_PAGE = 0;
  public static final int DEFAULT_RESULTS_PER_PAGE = 10;
  public static final String SEARCH_TERM = "q";
  public static final String QUERY_PARAM_ROWS = "rows";
  public static final String QUERY_PARAM_LANGUAGE = "language";
  public static final String DEFAULT_LANGUAGE = "en";
  public static final String I18N_KEY_LOAD_MORE_BUTTON_LABEL = "com.valtech.aem.saas.core.search.loadmore.button.label";

  @Self
  private SlingHttpServletRequest request;

  @OSGiService
  private FulltextSearchService fulltextSearchService;

  @OSGiService
  private FulltextSearchConfigurationService fulltextSearchConfigurationService;

  @OSGiService
  private I18nProvider i18nProvider;

  @ScriptVariable
  private Page currentPage;

  @JsonInclude(Include.NON_EMPTY)
  @Getter
  private String term;

  @JsonInclude(Include.NON_EMPTY)
  @Getter
  private int startPage;

  @JsonInclude(Include.NON_EMPTY)
  @Getter
  private int resultsPerPage;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private List<Result> results;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private int resultsTotal;

  @Getter
  private boolean showLoadMoreButton;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private Suggestion suggestion;

  private int configuredResultsPerPage;

  private I18n i18n;

  @PostConstruct
  private void init() {
    i18n = i18nProvider.getI18n(request);
    RequestConsumer requestConsumer = new RequestConsumer(request);
    requestConsumer.getParameter(SEARCH_TERM).ifPresent(t -> {
      term = t;
      Optional<Search> parentSearch = new ResourceConsumer(request.getResource())
          .getParentWithResourceType(RESOURCE_TYPE)
          .map(r -> r.adaptTo(Search.class));
      configuredResultsPerPage = parentSearch.map(Search::getResultsPerPage).orElse(DEFAULT_RESULTS_PER_PAGE);
      startPage = requestConsumer.getParameter(QUERY_PARAM_START)
          .map(s -> new StringToInteger(s).asInt())
          .map(OptionalInt::getAsInt)
          .orElse(DEFAULT_START_PAGE);
      resultsPerPage = resolveResultsPerPage(requestConsumer);
      SearchConfiguration searchConfiguration = request.getResource().adaptTo(ConfigurationBuilder.class)
          .as(SearchConfiguration.class);
      FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload =
          DefaultFulltextSearchRequestPayload.builder(new DefaultTermQuery(term),
                  new DefaultLanguageQuery(getLanguage(requestConsumer)))
              .optionalQuery(
                  new PaginationQuery(startPage, resultsPerPage, fulltextSearchConfigurationService.getRowsMaxLimit()))
              .optionalQuery(new HighlightingTagQuery(Highlighting.HIGHLIGHTING_TAG_NAME))
              .optionalQuery(FiltersQuery.builder()
                  .filterEntries(Arrays.stream(searchConfiguration.searchFilters()).collect(Collectors.toMap(
                      SearchFilterConfiguration::name, SearchFilterConfiguration::value))).build())
              .build();
      Optional<FulltextSearchResults> fulltextSearchResults = fulltextSearchService.getFulltextSearchConsumerService(
              searchConfiguration.index(),
              FulltextSearchConfigurationFactory.builder()
                  .enableAutoSuggest(parentSearch.map(Search::isAutoSuggestEnabled).orElse(false))
                  .enableBestBets(parentSearch.map(Search::isBestBetsEnabled).orElse(false))
                  .build().getConfiguration())
          .getResults(fulltextSearchGetRequestPayload);

      results = fulltextSearchResults.map(FulltextSearchResults::getResults).orElse(Collections.emptyList());
      resultsTotal = fulltextSearchResults.map(FulltextSearchResults::getTotalResultsFound).orElse(0);
      suggestion = fulltextSearchResults.map(FulltextSearchResults::getSuggestion).orElse(null);
    });
  }

  private int resolveResultsPerPage(RequestConsumer requestConsumer) {
    return requestConsumer.getParameter(QUERY_PARAM_ROWS)
        .map(s -> new StringToInteger(s).asInt())
        .map(OptionalInt::getAsInt)
        .orElse(configuredResultsPerPage);
  }

  @NonNull
  @Override
  public String getExportedType() {
    return request.getResource().getResourceType();
  }

  @JsonIgnore
  @Override
  public String getLoadMoreButtonText() {
    return i18n.get(I18N_KEY_LOAD_MORE_BUTTON_LABEL);
  }

  @JsonIgnore
  public int getLoadMoreRows() {
    return resultsPerPage + configuredResultsPerPage;
  }

  /**
   * Gets the language query value from the request parameter list or as fallback resolves the language from the current
   * page.
   *
   * @return current language.
   */
  private String getLanguage(RequestConsumer requestConsumer) {
    return requestConsumer.getParameter(QUERY_PARAM_LANGUAGE)
        .orElseGet(() -> currentPage.getLanguage().getLanguage());
  }
}
