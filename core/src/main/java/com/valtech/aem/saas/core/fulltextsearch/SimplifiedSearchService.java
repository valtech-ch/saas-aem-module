package com.valtech.aem.saas.core.fulltextsearch;

import com.day.cq.wcm.api.PageManagerFactory;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchGetRequestPayload;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchResults;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.api.fulltextsearch.SimplifiedSearch;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.DefaultTermQuery;
import com.valtech.aem.saas.core.query.FacetsQuery;
import com.valtech.aem.saas.core.query.FiltersQuery;
import com.valtech.aem.saas.core.query.HighlightingDisableQuery;
import com.valtech.aem.saas.core.query.HighlightingTagQuery;
import com.valtech.aem.saas.core.query.PaginationQuery;
import com.valtech.aem.saas.core.query.SimpleFilter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationResolver;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Slf4j
@Component(name = "Search as a Service - Simplified Search Service",
    service = SimplifiedSearch.class)
public class SimplifiedSearchService implements SimplifiedSearch {

  @Reference
  private FulltextSearchService fulltextSearchService;

  @Reference
  private ConfigurationResolver configurationResolver;

  @Reference
  private PageManagerFactory pageManagerFactory;

  @Override
  public Optional<FulltextSearchResults> getResults(Resource context, String searchText, int start, int rows,
      List<Filter> filters,
      List<String> facets) {
    SearchConfiguration searchConfiguration = configurationResolver.get(context).as(SearchConfiguration.class);
    DefaultFulltextSearchRequestPayload.Builder builder = DefaultFulltextSearchRequestPayload.builder(
            new DefaultTermQuery(searchText),
            new DefaultLanguageQuery(getLanguage(context)))
        .optionalQuery(
            new PaginationQuery(start, rows))
        .optionalQuery(StringUtils.isEmpty(searchConfiguration.highlightTagName())
            ? new HighlightingDisableQuery()
            : new HighlightingTagQuery(searchConfiguration.highlightTagName()));
    List<Filter> filtersList = getFilters(Arrays.stream(searchConfiguration.searchFilters())
            .map(searchFilterConfiguration -> new SimpleFilter(searchFilterConfiguration.name(),
                searchFilterConfiguration.value())),
        filters.stream());
    if (!filtersList.isEmpty()) {
      builder.optionalQuery(FiltersQuery.builder().filters(filtersList).build());
    }
    if (CollectionUtils.isNotEmpty(facets)) {
      builder.optionalQuery(FacetsQuery.builder().fields(facets).build());
    }
    FulltextSearchGetRequestPayload fulltextSearchGetRequestPayload = builder.build();
    return fulltextSearchService.getResults(searchConfiguration.index(), fulltextSearchGetRequestPayload);
  }

  private List<Filter> getFilters(Stream<Filter> contextFilters, Stream<Filter> specifiedFilters) {
    return Stream.concat(contextFilters, specifiedFilters).collect(Collectors.toList());
  }

  private String getLanguage(Resource context) {
    return pageManagerFactory.getPageManager(context.getResourceResolver())
        .getContainingPage(context)
        .getLanguage()
        .getLanguage();
  }
}
