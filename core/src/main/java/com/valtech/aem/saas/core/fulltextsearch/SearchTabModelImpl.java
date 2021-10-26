package com.valtech.aem.saas.core.fulltextsearch;


import static com.valtech.aem.saas.core.fulltextsearch.SearchTabModelImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.fulltextsearch.dto.DefaultFulltextSearchRequestPayloadDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchPayloadDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.ResultDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.SuggestionDTO;
import com.valtech.aem.saas.api.query.FiltersQuery;
import com.valtech.aem.saas.api.query.HighlightingTagQuery;
import com.valtech.aem.saas.api.query.LanguageQuery;
import com.valtech.aem.saas.api.query.PaginationQuery;
import com.valtech.aem.saas.api.query.TermQuery;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.http.response.dto.HighlightingDTO;
import com.valtech.aem.saas.core.i18n.I18nProvider;
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
    adapters = {SearchTabModel.class, ComponentExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchTabModelImpl implements SearchTabModel {

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
  private List<FilterModel> filters;

  private Set<FilterModel> mergedFilters;

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
  private List<ResultDTO> results;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private int resultsTotal;

  @Getter
  private boolean showLoadMoreButton;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private SuggestionDTO suggestion;

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
  public Set<FilterModel> getFilters() {
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
      FulltextSearchPayloadDTO fulltextSearchPayloadDto =
          getFulltextSearchGetRequestPayload(searchTerm, parentSearch);
      Optional<FulltextSearchResultsDTO> fulltextSearchResults = fulltextSearchService.getResults(
          searchConfiguration.index(), fulltextSearchPayloadDto, searchConfiguration.enableAutoSuggest(),
          searchConfiguration.enableBestBets());
      results = fulltextSearchResults.map(FulltextSearchResultsDTO::getResults).orElse(Collections.emptyList());
      resultsTotal = fulltextSearchResults.map(FulltextSearchResultsDTO::getTotalResultsFound).orElse(NO_RESULTS);
      showLoadMoreButton = !results.isEmpty() && results.size() < resultsTotal;
      suggestion = fulltextSearchResults.map(FulltextSearchResultsDTO::getSuggestion).orElse(null);
    });
  }

  private DefaultFulltextSearchRequestPayloadDTO getFulltextSearchGetRequestPayload(String searchTerm,
      SearchModel parentSearch) {
    return DefaultFulltextSearchRequestPayloadDTO.builder(new TermQuery(searchTerm),
            new LanguageQuery(getLanguage()))
        .optionalQuery(
            new PaginationQuery(startPage, resultsPerPage,
                fulltextSearchConfigurationService.getRowsMaxLimit()))
        .optionalQuery(new HighlightingTagQuery(HighlightingDTO.HIGHLIGHTING_TAG_NAME))
        .optionalQuery(FiltersQuery.builder()
            .filters(getMergedFilters(parentSearch))
            .build())
        .build();
  }

  private String getLoadMoreButtonText(SearchModel parentSearch, I18n i18n) {
    return Optional.ofNullable(parentSearch.getLoadMoreButtonText())
        .filter(StringUtils::isNotEmpty)
        .orElseGet(() -> i18n.get(I18N_KEY_LOAD_MORE_BUTTON_LABEL));
  }

  private int getConfiguredResultsPerPage(SearchModel parentSearch) {
    return parentSearch.getResultsPerPage();
  }

  private Set<FilterModel> getMergedFilters(SearchModel search) {
    Set<FilterModel> result = search.getFilters();
    if (filters != null) {
      result.addAll(filters);
      return result;
    }
    return Collections.emptySet();
  }

  private Optional<SearchModel> getParentSearchComponent() {
    return Optional.ofNullable(request.getResource().adaptTo(ResourceWrapper.class))
        .flatMap(r -> r.getParentWithResourceType(SearchModelImpl.RESOURCE_TYPE))
        .map(r -> r.adaptTo(SearchModel.class));
  }

  private String getLanguage() {
    return currentPage.getLanguage().getLanguage();
  }
}
