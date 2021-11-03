package com.valtech.aem.saas.core.fulltextsearch;


import static com.valtech.aem.saas.core.fulltextsearch.SearchTabModelImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.FacetModel;
import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFieldResultDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFieldResultsDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFilterDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFilterOptionDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FacetFiltersDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.FulltextSearchResultsDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.ResultDTO;
import com.valtech.aem.saas.api.fulltextsearch.dto.SuggestionDTO;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FilterFactory;
import com.valtech.aem.saas.api.query.SimpleFilter;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import com.valtech.aem.saas.core.util.StringToInteger;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Search tab component sling model that handles component's rendering.
 */
@Slf4j
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
  public static final String I18N_KEY_LOAD_MORE_BUTTON_LABEL = "com.valtech.aem.saas.core.search.loadmore.button.label";
  public static final int NO_RESULTS = 0;
  public static final String FACET_FILTER = "facetFilter";

  @Getter
  @JsonInclude(Include.NON_EMPTY)
  @ValueMapValue
  private String title;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private SuggestionDTO suggestion;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private List<ResultDTO> results;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private int resultsTotal;

  @Getter
  private boolean showLoadMoreButton;

  @Getter
  @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
  private String exportedType;

  @JsonIgnore
  @Getter
  @ChildResource
  private List<FilterModel> filters;

  @Self
  private SlingHttpServletRequest request;

  @OSGiService
  private I18nProvider i18nProvider;

  @OSGiService
  private FulltextSearchService fulltextSearchService;

  @JsonInclude(Include.NON_NULL)
  @Getter
  private FacetFiltersDTO facetFilters;

  @ChildResource
  private List<FacetModel> facets;

  private int startPage;

  private int resultsPerPage;

  @PostConstruct
  private void init() {
    Optional<RequestWrapper> requestWrapper = getRequestWrapper();
    requestWrapper
        .flatMap(this::getSearchTerm)
        .ifPresent(searchTerm -> initSearch(searchTerm, requestWrapper.get()));
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
      startPage = getStartPage(requestWrapper);
      resultsPerPage = getConfiguredResultsPerPage(parentSearch);
      SearchCAConfigurationModel searchCAConfigurationModel = requestWrapper.getResource().adaptTo(
          SearchCAConfigurationModel.class);
      if (searchCAConfigurationModel != null) {
        Optional<FulltextSearchResultsDTO> fulltextSearchResults = fulltextSearchService.getResults(
            searchCAConfigurationModel,
            searchTerm, requestWrapper.getLocale().getLanguage(), startPage, resultsPerPage,
            getEffectiveFilters(parentSearch, requestWrapper), Optional.ofNullable(facets).map(List::stream).orElse(
                Stream.empty()).map(FacetModel::getFieldName).collect(
                Collectors.toSet()));
        results = fulltextSearchResults.map(FulltextSearchResultsDTO::getResults).orElse(Collections.emptyList());
        resultsTotal = fulltextSearchResults.map(FulltextSearchResultsDTO::getTotalResultsFound).orElse(NO_RESULTS);
        showLoadMoreButton = !results.isEmpty() && results.size() < resultsTotal;
        suggestion = fulltextSearchResults.map(FulltextSearchResultsDTO::getSuggestion).orElse(null);
        facetFilters = fulltextSearchResults.map(FulltextSearchResultsDTO::getFacetFieldsResults)
            .map(this::getFacetFilters).orElse(null);
      } else {
        log.error("Could not resolve context aware search configurations from current request.");
      }
    });
  }

  private FacetFiltersDTO getFacetFilters(List<FacetFieldResultsDTO> facetFieldResultsDTOList) {
    if (isFacetsConfigured()) {
      Map<String, String> facetFieldToLabelMap = getFacetFieldToLabelMap();
      List<FacetFilterDTO> facetFilterDTOList = facetFieldResultsDTOList.stream()
          .map(facetFieldResultsDTO -> createFacetFilter(facetFieldToLabelMap, facetFieldResultsDTO))
          .collect(Collectors.toList());
      return CollectionUtils.isNotEmpty(facetFilterDTOList) ? new FacetFiltersDTO(FACET_FILTER, facetFilterDTOList)
          : null;
    }
    return null;
  }

  private FacetFilterDTO createFacetFilter(Map<String, String> facetFieldToLabelMap,
      FacetFieldResultsDTO facetFieldResultsDTO) {
    return new FacetFilterDTO(facetFieldToLabelMap.get(facetFieldResultsDTO.getFieldName()),
        facetFieldResultsDTO.getFieldName(),
        facetFieldResultsDTO.getItems().stream()
            .map(this::createFacetFilterOption)
            .collect(Collectors.toList()));
  }

  private FacetFilterOptionDTO createFacetFilterOption(FacetFieldResultDTO facetFieldResultDTO) {
    return new FacetFilterOptionDTO(facetFieldResultDTO.getText(), facetFieldResultDTO.getCount());
  }

  private boolean isFacetsConfigured() {
    return CollectionUtils.isNotEmpty(facets);
  }

  private Map<String, String> getFacetFieldToLabelMap() {
    return Optional.ofNullable(facets)
        .map(List::stream)
        .orElse(Stream.empty())
        .collect(Collectors.toMap(FacetModel::getFieldName, FacetModel::getLabel));
  }

  private int getConfiguredResultsPerPage(SearchModel parentSearch) {
    return parentSearch.getResultsPerPage();
  }

  private Set<Filter> getEffectiveFilters(SearchModel search, RequestWrapper requestWrapper) {
    Set<Filter> result = search.getEffectiveFilters();
    result.addAll(getConfigureFilters());
    result.addAll(getSelectedFacetFilters(requestWrapper));
    return result;
  }

  private Set<Filter> getConfigureFilters() {
    return Optional.ofNullable(filters).map(List::stream).orElse(Stream.empty())
        .map(f -> new SimpleFilter(f.getName(), f.getValue())).collect(Collectors.toSet());
  }

  private Set<Filter> getSelectedFacetFilters(RequestWrapper requestWrapper) {
    return requestWrapper.getParameterValues(FACET_FILTER).stream()
        .map(this::createFilter)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  private Filter createFilter(String facetFilterEntry) {
    FacetFilterParser facetFilterParser = new FacetFilterParser(facetFilterEntry);
    if (StringUtils.isNotEmpty(facetFilterParser.getKey()) && CollectionUtils.isNotEmpty(
        facetFilterParser.getValues())) {
      return FilterFactory.createFilter(facetFilterParser.getKey(), facetFilterParser.getValues());
    }
    return null;
  }

  private Optional<SearchModel> getParentSearchComponent() {
    return Optional.ofNullable(request.getResource().adaptTo(ResourceWrapper.class))
        .flatMap(r -> r.getParentWithResourceType(SearchModelImpl.RESOURCE_TYPE))
        .map(r -> r.adaptTo(SearchModel.class));
  }
}
