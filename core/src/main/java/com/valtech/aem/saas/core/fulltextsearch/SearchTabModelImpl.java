package com.valtech.aem.saas.core.fulltextsearch;


import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.fulltextsearch.dto.*;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.FilterFactory;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.util.ResourceUtil;
import com.valtech.aem.saas.core.util.StringToInteger;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.valtech.aem.saas.core.fulltextsearch.SearchTabModelImpl.RESOURCE_TYPE;

/**
 * Search tab component sling model that handles component's rendering.
 */
@Slf4j
@Model(adaptables = SlingHttpServletRequest.class,
       adapters = {SearchTabModel.class, ComponentExporter.class},
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
       resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
          extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchTabModelImpl implements SearchTabModel, ComponentExporter {

    public static final String RESOURCE_TYPE = "saas-aem-module/components/searchtab";
    public static final int DEFAULT_START_PAGE = 1;
    public static final int DEFAULT_RESULTS_PER_PAGE = 10;
    public static final String I18N_KEY_LOAD_MORE_BUTTON_LABEL =
            "com.valtech.aem.saas.core.search.loadmore.button.label";

    @Getter
    @JsonInclude(Include.NON_EMPTY)
    @ValueMapValue
    private String title;

    @Getter
    @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
    private String exportedType;

    @Getter
    @ValueMapValue
    private String template;

    @JsonInclude(Include.NON_NULL)
    @Getter
    private SuggestionDTO suggestion;

    @JsonInclude(Include.NON_NULL)
    @Getter
    private List<ResultDTO> results;

    @JsonInclude(Include.NON_DEFAULT)
    @Getter
    private int resultsTotal;

    @JsonInclude(Include.NON_DEFAULT)
    @Getter
    private boolean showLoadMoreButton;

    @JsonInclude(Include.NON_EMPTY)
    @Getter
    private String url;

    @JsonInclude(Include.NON_NULL)
    @Getter
    private FacetFiltersDTO facetFilters;

    @ChildResource(name = "filters")
    private List<FilterConfigurationModel> configuredFilters;

    @ValueMapValue
    private String language;
    

    @JsonInclude(Include.NON_DEFAULT)
    @Getter
    @ValueMapValue
    private String tabId;

    @ValueMapValue
    private boolean disableContextFilters;

    @ChildResource
    private List<FacetModel> facets;

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @OSGiService
    private FulltextSearchService fulltextSearchService;

    @OSGiService
    private PathTransformer pathTransformer;

    private SearchModel parentSearch;

    private String searchTerm;

    private RequestWrapper requestWrapper;

    private int resultsPerPage;

    private int resultsPage;

    @PostConstruct
    private void init() {
        constructJsonExportUrl().ifPresent(u -> url = u);
        getRequestWrapper().ifPresent(rw -> requestWrapper = rw);
        getSearchTerm().ifPresent(s -> searchTerm = s);
        getParentSearchComponent().ifPresent(cmp -> parentSearch = cmp);
        resultsPerPage = getConfiguredResultsPerPage().orElse(DEFAULT_RESULTS_PER_PAGE);
        resultsPage = getResultsPage(requestWrapper);
        getFulltextSearchResults().ifPresent(fulltextSearchResults -> {
            results = fulltextSearchResults.getResults();
            resultsTotal = fulltextSearchResults.getTotalResultsFound();
            showLoadMoreButton = resultsPage * resultsPerPage < resultsTotal;
            suggestion = fulltextSearchResults.getSuggestion();
            facetFilters = getFacetFilters(fulltextSearchResults.getFacetFieldsResults());
        });
        tabId = StringUtils.isNotBlank(tabId) ? tabId : ResourceUtil.generateId("tab", resource.getPath());
    }

    private Optional<Integer> getConfiguredResultsPerPage() {
        return Optional.ofNullable(parentSearch).map(SearchModel::getResultsPerPage);
    }

    private Optional<String> constructJsonExportUrl() {
        try {
            return Optional.of(new URIBuilder(String.format("%s.%s.%s",
                                                            pathTransformer.map(request, resource.getPath()),
                                                            ExporterConstants.SLING_MODEL_SELECTOR,
                                                            ExporterConstants.SLING_MODEL_EXTENSION))
                                       .setCustomQuery(request.getQueryString())
                                       .toString());
        } catch (URISyntaxException e) {
            log.error("Failed to create search prepared url to search tab resource.", e);
        }
        return Optional.empty();
    }

    private Optional<RequestWrapper> getRequestWrapper() {
        return Optional.ofNullable(request).map(r -> r.adaptTo(RequestWrapper.class));
    }

    private Optional<String> getSearchTerm() {
        return Optional.ofNullable(requestWrapper).flatMap(r -> r.getParameter(SearchTabModel.QUERY_PARAM_SEARCH_TERM));
    }

    private int getResultsPage(RequestWrapper requestWrapper) {
        return Optional.ofNullable(requestWrapper)
                       .flatMap(rw -> rw.getParameter(SearchTabModel.QUERY_PARAM_PAGE))
                       .map(s -> new StringToInteger(s).asInt())
                       .filter(OptionalInt::isPresent)
                       .map(OptionalInt::getAsInt)
                       .orElse(DEFAULT_START_PAGE);
    }

    private int resolveStartOffset() {
        return (resultsPage - 1) * resultsPerPage;
    }

    private Optional<FulltextSearchResultsDTO> getFulltextSearchResults() {
        if (isResourceOverriddenRequest()) {
            log.trace(
                    String.join(StringUtils.EMPTY,
                                "Skip querying for search results in case this sling model is initialized with a " +
                                        "request",
                                " when request.getRequestPathInfo().getResourcePath() != request.getResource()" +
                                        ".getPath().",
                                " (Possible when the request is of type org.apache.sling.models.impl" +
                                        ".ResourceOverridingRequestWrapper)"));
            return Optional.empty();
        }
        if (StringUtils.isBlank(searchTerm)) {
            log.debug("No search term is passed in the request.");
            return Optional.empty();
        }
        SearchCAConfigurationModel searchCAConfigurationModel = resource.adaptTo(SearchCAConfigurationModel.class);
        if (searchCAConfigurationModel == null) {
            log.error("Could not resolve context aware search configurations from current request.");
            return Optional.empty();
        }
        if (parentSearch == null) {
            log.error("Could not resolve the parent search component. (This is highly unlikely. It suggests content " +
                              "corruption.)");
            return Optional.empty();
        }
        if (requestWrapper == null) {
            log.error("Could not adapt the request to com.valtech.aem.saas.core.common.request.RequestWrapper. (This " +
                              "should never happen.)");
            return Optional.empty();
        }
        return fulltextSearchService.getResults(
                searchCAConfigurationModel,
                searchTerm,
                StringUtils.defaultIfBlank(language, parentSearch.getLanguage()),
                resolveStartOffset(),
                resultsPerPage,
                getEffectiveFilters(parentSearch, requestWrapper),
                Optional.ofNullable(facets).map(List::stream).orElse(
                        Stream.empty()).map(FacetModel::getFieldName).collect(
                        Collectors.toCollection(LinkedHashSet::new)),
                disableContextFilters || parentSearch.isDisableContextFilters(),
                template);
    }

    private boolean isResourceOverriddenRequest() {
        return !StringUtils.equals(request.getRequestPathInfo().getResourcePath(), resource.getPath());
    }

    private FacetFiltersDTO getFacetFilters(List<FacetFieldResultsDTO> facetFieldResultsDTOList) {
        if (isFacetsConfigured()) {
            Map<String, String> facetFieldToLabelMap = getFacetFieldToLabelMap();
            List<FacetFilterDTO> facetFilterDTOList = facetFieldResultsDTOList.stream()
                                                                              .map(facetFieldResultsDTO -> createFacetFilter(
                                                                                      facetFieldToLabelMap,
                                                                                      facetFieldResultsDTO))
                                                                              .collect(Collectors.toList());
            return CollectionUtils.isNotEmpty(facetFilterDTOList)
                    ? new FacetFiltersDTO(SearchTabModel.QUERY_PARAM_FACET_FILTER,
                                          facetFilterDTOList)
                    : null;
        }
        return null;
    }

    private FacetFilterDTO createFacetFilter(
            Map<String, String> facetFieldToLabelMap,
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

    private Set<Filter> getEffectiveFilters(
            SearchModel search,
            RequestWrapper requestWrapper) {
        Set<Filter> result = search.getFilters();
        result.addAll(getConfiguredFilters());
        result.addAll(getSelectedFacetFilters(requestWrapper));
        return result;
    }

    private Set<Filter> getConfiguredFilters() {
        return Optional.ofNullable(configuredFilters)
                       .map(List::stream)
                       .orElse(Stream.empty())
                       .filter(FilterConfiguration::isValid)
                       .map(FilterConfiguration::getFilter)
                       .filter(Objects::nonNull)
                       .collect(Collectors.toSet());
    }

    private Set<Filter> getSelectedFacetFilters(RequestWrapper requestWrapper) {
        return requestWrapper.getParameterValues(SearchTabModel.QUERY_PARAM_FACET_FILTER).stream()
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
        return Optional.ofNullable(resource.adaptTo(ResourceWrapper.class))
                       .flatMap(r -> r.getParentWithResourceType(SearchModelImpl.RESOURCE_TYPE))
                       .map(r -> r.adaptTo(SearchModel.class));
    }

}
