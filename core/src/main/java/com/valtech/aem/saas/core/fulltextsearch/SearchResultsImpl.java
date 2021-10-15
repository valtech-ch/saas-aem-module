package com.valtech.aem.saas.core.fulltextsearch;


import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.caconfig.SearchFilterConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.*;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.http.response.Highlighting;
import com.valtech.aem.saas.core.query.*;
import com.valtech.aem.saas.core.util.StringToInteger;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

import static com.valtech.aem.saas.core.fulltextsearch.SearchResultsImpl.RESOURCE_TYPE;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = {SearchResults.class, ComponentExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchResultsImpl implements SearchResults {

  public static final String RESOURCE_TYPE = "saas-aem-module/components/searchresults";
  public static final String QUERY_PARAM_START = "start";
  public static final int DEFAULT_START_PAGE = 0;
  public static final int DEFAULT_RESULTS_PER_PAGE = 10;
  public static final String SEARCH_TERM = "q";
  public static final String QUERY_PARAM_ROWS = "rows";
  public static final String I18N_KEY_LOAD_MORE_BUTTON_LABEL = "com.valtech.aem.saas.core.search.loadmore.button.label";

  @Self
  private SlingHttpServletRequest request;

  @OSGiService
  private FulltextSearchService fulltextSearchService;

  @OSGiService
  private FulltextSearchConfigurationService fulltextSearchConfigurationService;

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

  private int configuredResultsPerPage;

  @Getter
  @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
  private String exportedType;

  private I18n i18n;

  @JsonIgnore
  @Getter
  private int loadMoreRows;

  @PostConstruct
  private void init() {
    configuredResultsPerPage = getConfiguredResultsPerPage();
    Optional.ofNullable(request.adaptTo(RequestWrapper.class))
        .ifPresent(requestWrapper -> {
          i18n = requestWrapper.getI18n();
          requestWrapper.getParameter(SEARCH_TERM).ifPresent(s -> term = s);
          startPage = requestWrapper.getParameter(QUERY_PARAM_START)
              .map(s -> new StringToInteger(s).asInt())
              .map(OptionalInt::getAsInt)
              .orElse(DEFAULT_START_PAGE);
          resultsPerPage = resolveResultsPerPage(requestWrapper);
          //todo: remove this when ICSAAS-315 is done - currently utilized only for demo purpose
          loadMoreRows = resultsPerPage + configuredResultsPerPage;
          SearchConfiguration searchConfiguration = request.getResource().adaptTo(ConfigurationBuilder.class)
              .as(SearchConfiguration.class);
          FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload =
              DefaultFulltextSearchRequestPayload.builder(new DefaultTermQuery(term),
                      new DefaultLanguageQuery(getLanguage()))
                  .optionalQuery(
                      new PaginationQuery(startPage, resultsPerPage,
                          fulltextSearchConfigurationService.getRowsMaxLimit()))
                  .optionalQuery(new HighlightingTagQuery(Highlighting.HIGHLIGHTING_TAG_NAME))
                  .optionalQuery(FiltersQuery.builder()
                      .filterEntries(Arrays.stream(searchConfiguration.searchFilters()).collect(Collectors.toMap(
                          SearchFilterConfiguration::name, SearchFilterConfiguration::value))).build())
                  .build();
          results = fulltextSearchService.getResults(searchConfiguration.index(), fulltextSearchGetRequestPayload)
              .map(FulltextSearchResults::getResults).orElse(
                  Collections.emptyList());
        });
  }

  private int resolveResultsPerPage(RequestWrapper requestWrapper) {
    return requestWrapper.getParameter(QUERY_PARAM_ROWS)
        .map(s -> new StringToInteger(s).asInt())
        .map(OptionalInt::getAsInt)
        .orElse(configuredResultsPerPage);
  }

  @JsonIgnore
  @Override
  public String getLoadMoreButtonText() {
    return Optional.ofNullable(i18n).map(t -> t.get(I18N_KEY_LOAD_MORE_BUTTON_LABEL)).orElse(StringUtils.EMPTY);
  }

  private int getConfiguredResultsPerPage() {
    return Optional.ofNullable(request.getResource().adaptTo(ResourceWrapper.class))
        .flatMap(r -> r.getParentWithResourceType(SearchImpl.RESOURCE_TYPE))
        .map(r -> r.adaptTo(Search.class))
        .map(Search::getResultsPerPage).orElse(DEFAULT_RESULTS_PER_PAGE);
  }

  private String getLanguage() {
    return currentPage.getLanguage().getLanguage();
  }
}
