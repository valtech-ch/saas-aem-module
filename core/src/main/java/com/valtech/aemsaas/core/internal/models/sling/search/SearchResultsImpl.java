package com.valtech.aemsaas.core.internal.models.sling.search;

import static com.valtech.aemsaas.core.internal.models.sling.search.SearchImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aemsaas.core.models.search.payload.DefaultFulltextSearchGetRequestPayload;
import com.valtech.aemsaas.core.models.search.payload.FulltextSearchGetRequestPayload;
import com.valtech.aemsaas.core.models.search.query.DefaultLanguageQuery;
import com.valtech.aemsaas.core.models.search.query.DefaultTermQuery;
import com.valtech.aemsaas.core.models.search.query.HighlightingTagQuery;
import com.valtech.aemsaas.core.models.search.query.PaginationQuery;
import com.valtech.aemsaas.core.models.search.results.FulltextSearchResults;
import com.valtech.aemsaas.core.models.search.results.Result;
import com.valtech.aemsaas.core.models.sling.search.Search;
import com.valtech.aemsaas.core.models.sling.search.SearchResults;
import com.valtech.aemsaas.core.services.search.FulltextSearchConfigurationService;
import com.valtech.aemsaas.core.services.search.FulltextSearchService;
import com.valtech.aemsaas.core.utils.StringToInteger;
import com.valtech.aemsaas.core.utils.request.RequestParameters;
import com.valtech.aemsaas.core.utils.resource.ParentResource;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
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

  @PostConstruct
  private void init() {
    RequestParameters requestParametrs = new RequestParameters(request);
    term = requestParametrs.getParameter("term");
    startPage = new StringToInteger(requestParametrs.getParameter("start")).asInt().orElse(0);
    resultsPerPage = resolveResultsPerPage();
//    SearchConfiguration searchConfiguration = resource.adaptTo(ConfigurationBuilder.class).as(SearchConfiguration.class);
    FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload =
        DefaultFulltextSearchGetRequestPayload.builder(new DefaultTermQuery(term), new DefaultLanguageQuery("de"))
            .optionalQuery(
                new PaginationQuery(startPage, resultsPerPage, fulltextSearchConfigurationService.getRowsMaxLimit()))
            .optionalQuery(new HighlightingTagQuery("em"))
            .build();
    results = fulltextSearchService.getResults("/pgweb", fulltextSearchGetRequestPayload)
        .map(FulltextSearchResults::getResults).orElse(
            Collections.emptyList());
  }

  private int resolveResultsPerPage() {
    return Optional.ofNullable(new RequestParameters(request).getParameter("rows"))
        .filter(StringUtils::isNotEmpty)
        .map(s -> new StringToInteger(s).asInt())
        .map(OptionalInt::getAsInt)
        .orElseGet(() -> new ParentResource(request.getResource())
            .getParentWithResourceType(RESOURCE_TYPE)
            .map(r -> r.adaptTo(Search.class))
            .map(Search::getResultsPerPage).orElse(10));
  }

  @NonNull
  @Override
  public String getExportedType() {
    return request.getResource().getResourceType();
  }
}
