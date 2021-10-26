package com.valtech.aem.saas.core.fulltextsearch;


import static com.valtech.aem.saas.core.fulltextsearch.SearchTabImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.Filter;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchResults;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.Result;
import com.valtech.aem.saas.api.fulltextsearch.Search;
import com.valtech.aem.saas.api.fulltextsearch.SearchTab;
import com.valtech.aem.saas.api.fulltextsearch.Suggestion;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.http.response.Highlighting;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.DefaultTermQuery;
import com.valtech.aem.saas.core.query.FiltersQuery;
import com.valtech.aem.saas.core.query.HighlightingTagQuery;
import com.valtech.aem.saas.core.query.PaginationQuery;
import com.valtech.aem.saas.core.util.StringToInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Search tab component sling model that handles component's rendering.
 */
@Model(adaptables = SlingHttpServletRequest.class,
    adapters = {SearchTab.class, ComponentExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchTabImpl implements SearchTab {

  public static final String RESOURCE_TYPE = "saas-aem-module/components/searchtab";
  public static final String QUERY_PARAM_START = "start";
  public static final int DEFAULT_START_PAGE = 0;
  public static final int DEFAULT_RESULTS_PER_PAGE = 10;
  public static final String SEARCH_TERM = "q";
  public static final String QUERY_PARAM_ROWS = "rows";
  public static final String I18N_KEY_LOAD_MORE_BUTTON_LABEL = "com.valtech.aem.saas.core.search.loadmore.button.label";
  public static final int NO_RESULTS = 0;

  @Self
  private SlingHttpServletRequest request;

  @OSGiService
  protected I18nProvider i18nProvider;

  @OSGiService
  private FulltextSearchService fulltextSearchService;

  @OSGiService
  private FulltextSearchConfigurationService fulltextSearchConfigurationService;

  @ScriptVariable
  private Page currentPage;

  @Getter
  @JsonInclude(Include.NON_EMPTY)
  @ValueMapValue
  private String title;

  @ChildResource
  private List<Filter> filters;

  private Set<Filter> mergedFilters;

  @JsonIgnore
  @JsonInclude(Include.NON_EMPTY)
  @Getter
  private String term;

  @JsonIgnore
  @Getter
  private int startPage;

  @JsonIgnore
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

  @Getter
  @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
  private String exportedType;

  @JsonIgnore
  @Getter
  private int loadMoreRows;

  @JsonIgnore
  @Getter
  private String loadMoreButtonText;

  @PostConstruct
  private void init() {
    Optional<RequestWrapper> requestWrapper = getRequestWrapper();
    requestWrapper
        .flatMap(this::getSearchTerm)
        .ifPresent(searchTerm -> initSearch(searchTerm, requestWrapper.get()));
  }

  @JsonIgnore
  @Override
  public Set<Filter> getFilters() {
    return mergedFilters;
  }

  private Optional<RequestWrapper> getRequestWrapper() {
    return Optional.ofNullable(request.adaptTo(RequestWrapper.class));
  }

  private Optional<String> getSearchTerm(RequestWrapper requestWrapper) {
    return requestWrapper.getParameter(SEARCH_TERM).filter(StringUtils::isNotBlank);
  }

  private int getStartPage(RequestWrapper requestWrapper) {
    return requestWrapper.getParameter(QUERY_PARAM_START)
        .map(start -> new StringToInteger(start).asInt())
        .map(OptionalInt::getAsInt)
        .orElse(DEFAULT_START_PAGE);
  }

  private void initSearch(String searchTerm, RequestWrapper requestWrapper) {
    getParentSearchComponent().ifPresent(parentSearch -> {
      term = searchTerm;
      I18n i18n = i18nProvider.getI18n(requestWrapper.getLocale());
      loadMoreButtonText = getLoadMoreButtonText(parentSearch, i18n);
      startPage = getStartPage(requestWrapper);
      resultsPerPage = getConfiguredResultsPerPage(parentSearch);
      SearchConfiguration searchConfiguration = request.getResource().adaptTo(ConfigurationBuilder.class)
          .as(SearchConfiguration.class);
      FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload =
          getFulltextSearchGetRequestPayload(searchTerm, parentSearch);
      Optional<FulltextSearchResults> fulltextSearchResults = fulltextSearchService.getResults(
          searchConfiguration.index(), fulltextSearchGetRequestPayload, searchConfiguration.enableAutoSuggest(),
          searchConfiguration.enableBestBets());
      results = fulltextSearchResults.map(FulltextSearchResults::getResults).orElse(Collections.emptyList());
      resultsTotal = fulltextSearchResults.map(FulltextSearchResults::getTotalResultsFound).orElse(NO_RESULTS);
      showLoadMoreButton = !results.isEmpty() && results.size() < resultsTotal;
      suggestion = fulltextSearchResults.map(FulltextSearchResults::getSuggestion).orElse(null);
    });
  }

  private DefaultFulltextSearchRequestPayload getFulltextSearchGetRequestPayload(String searchTerm,
      Search parentSearch) {
    return DefaultFulltextSearchRequestPayload.builder(new DefaultTermQuery(searchTerm),
            new DefaultLanguageQuery(getLanguage()))
        .optionalQuery(
            new PaginationQuery(startPage, resultsPerPage,
                fulltextSearchConfigurationService.getRowsMaxLimit()))
        .optionalQuery(new HighlightingTagQuery(Highlighting.HIGHLIGHTING_TAG_NAME))
        .optionalQuery(FiltersQuery.builder()
            .filters(getMergedFilters(parentSearch))
            .build())
        .build();
  }

  private String getLoadMoreButtonText(Search parentSearch, I18n i18n) {
    return Optional.ofNullable(parentSearch.getLoadMoreButtonText())
        .filter(StringUtils::isNotEmpty)
        .orElseGet(() -> i18n.get(I18N_KEY_LOAD_MORE_BUTTON_LABEL));
  }

  private int getConfiguredResultsPerPage(Search parentSearch) {
    return parentSearch.getResultsPerPage();
  }

  private Set<Filter> getMergedFilters(Search search) {
    Set<Filter> result = search.getFilters();
    if (filters != null) {
      result.addAll(filters);
      return result;
    }
    return Collections.emptySet();
  }

  private Optional<Search> getParentSearchComponent() {
    return Optional.ofNullable(request.getResource().adaptTo(ResourceWrapper.class))
        .flatMap(r -> r.getParentWithResourceType(SearchImpl.RESOURCE_TYPE))
        .map(r -> r.adaptTo(Search.class));
  }

  private String getLanguage() {
    return currentPage.getLanguage().getLanguage();
  }
}
