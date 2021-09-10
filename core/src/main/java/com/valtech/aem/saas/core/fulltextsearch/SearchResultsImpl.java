package com.valtech.aem.saas.core.fulltextsearch;

import static com.valtech.aem.saas.api.fulltextsearch.Search.DEFAULT_RESULTS_PER_PAGE;
import static com.valtech.aem.saas.api.fulltextsearch.Search.DEFAULT_START_PAGE;
import static com.valtech.aem.saas.api.fulltextsearch.Search.SEARCH_TERM;
import static com.valtech.aem.saas.core.fulltextsearch.SearchImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
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
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.DefaultTermQuery;
import com.valtech.aem.saas.core.query.FiltersQuery;
import com.valtech.aem.saas.core.query.HighlightingTagQuery;
import com.valtech.aem.saas.core.query.PaginationQuery;
import com.valtech.aem.saas.core.util.StringToInteger;
import com.valtech.aem.saas.core.util.request.RequestParameters;
import com.valtech.aem.saas.core.util.resource.ParentResource;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = {SearchResults.class, ComponentExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchResultsImpl implements SearchResults {

  public static final String RESOURCE_TYPE = "saas-aem-module/components/saas/searchresults";


  @Self
  private SlingHttpServletRequest request;

  @OSGiService
  private FulltextSearchService fulltextSearchService;

  @OSGiService
  private FulltextSearchConfigurationService fulltextSearchConfigurationService;

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

  @PostConstruct
  private void init() {
    configuredResultsPerPage = getConfiguredResultsPerPage();
    RequestParameters requestParametrs = new RequestParameters(request);
    term = requestParametrs.getParameter(SEARCH_TERM);
    if (StringUtils.isNotBlank(term)) {
      startPage = new StringToInteger(requestParametrs.getParameter("start")).asInt().orElse(DEFAULT_START_PAGE);
      resultsPerPage = resolveResultsPerPage();
      SearchConfiguration searchConfiguration = request.getResource().adaptTo(ConfigurationBuilder.class)
          .as(SearchConfiguration.class);
      FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload =
          DefaultFulltextSearchRequestPayload.builder(new DefaultTermQuery(term), new DefaultLanguageQuery("de"))
              .optionalQuery(
                  new PaginationQuery(startPage, resultsPerPage, fulltextSearchConfigurationService.getRowsMaxLimit()))
              .optionalQuery(new HighlightingTagQuery("em"))
              .optionalQuery(FiltersQuery.builder()
                  .filterEntries(Arrays.stream(searchConfiguration.searchFilters()).collect(Collectors.toMap(
                      SearchFilterConfiguration::name, SearchFilterConfiguration::value))).build())
              .build();
      results = fulltextSearchService.getResults(searchConfiguration.index(), fulltextSearchGetRequestPayload)
          .map(FulltextSearchResults::getResults).orElse(
              Collections.emptyList());
    }
  }

  private int resolveResultsPerPage() {
    return Optional.ofNullable(new RequestParameters(request).getParameter("rows"))
        .filter(StringUtils::isNotEmpty)
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
    return new ParentResource(request.getResource()).getParentWithResourceType(SearchImpl.RESOURCE_TYPE)
        .map(r -> r.adaptTo(Search.class))
        .map(Search::getLoadMoreButtonText)
        .orElse(StringUtils.EMPTY);
  }

  @JsonIgnore
  public int getLoadMoreRows() {
    return resultsPerPage + configuredResultsPerPage;
  }

  private int getConfiguredResultsPerPage() {
    return new ParentResource(request.getResource())
        .getParentWithResourceType(RESOURCE_TYPE)
        .map(r -> r.adaptTo(Search.class))
        .map(Search::getResultsPerPage).orElse(DEFAULT_RESULTS_PER_PAGE);
  }
}
