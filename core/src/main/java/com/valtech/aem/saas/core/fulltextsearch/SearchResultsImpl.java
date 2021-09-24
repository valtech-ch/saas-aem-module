package com.valtech.aem.saas.core.fulltextsearch;


import static com.valtech.aem.saas.core.fulltextsearch.SearchResultsImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.i18n.I18n;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchResults;
import com.valtech.aem.saas.api.fulltextsearch.Result;
import com.valtech.aem.saas.api.fulltextsearch.Search;
import com.valtech.aem.saas.api.fulltextsearch.SearchResults;
import com.valtech.aem.saas.api.fulltextsearch.SimplifiedSearch;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.util.StringToInteger;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.PostConstruct;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

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
  public static final String I18N_KEY_LOAD_MORE_BUTTON_LABEL = "com.valtech.aem.saas.core.search.loadmore.button.label";

  @Self
  private SlingHttpServletRequest request;

  @OSGiService
  private SimplifiedSearch simplifiedSearch;

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
          requestWrapper.getParameter(SEARCH_TERM).ifPresent(s -> {
            term = s;
            startPage = requestWrapper.getParameter(QUERY_PARAM_START)
                .map(start -> new StringToInteger(start).asInt())
                .map(OptionalInt::getAsInt)
                .orElse(DEFAULT_START_PAGE);
            resultsPerPage = resolveResultsPerPage(requestWrapper);
            //todo: remove this when ICSAAS-315 is done - currently utilized only for demo purpose
            loadMoreRows = resultsPerPage + configuredResultsPerPage;
            results = simplifiedSearch.getResults(request.getResource(), s, startPage, resultsPerPage)
                .map(FulltextSearchResults::getResults)
                .orElse(Collections.emptyList());
          });
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
        .flatMap(r -> r.getParentWithResourceType(RESOURCE_TYPE))
        .map(r -> r.adaptTo(Search.class))
        .map(Search::getResultsPerPage).orElse(DEFAULT_RESULTS_PER_PAGE);
  }

}
