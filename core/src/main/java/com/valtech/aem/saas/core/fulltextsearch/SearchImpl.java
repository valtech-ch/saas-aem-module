package com.valtech.aem.saas.core.fulltextsearch;

import static com.valtech.aem.saas.core.fulltextsearch.SearchImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.i18n.I18n;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.valtech.aem.saas.api.fulltextsearch.Filter;
import com.valtech.aem.saas.api.fulltextsearch.Search;
import com.valtech.aem.saas.api.fulltextsearch.SearchResults;
import com.valtech.aem.saas.core.common.request.RequestConsumer;
import com.valtech.aem.saas.core.common.resource.ResourceConsumer;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {Search.class, ComponentExporter.class, ContainerExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION,
    options = {
        @ExporterOption(name = "SerializationFeature.INDENT_OUTPUT", value = "true")
    })
public class SearchImpl implements Search {

  public static final String RESOURCE_TYPE = "saas-aem-module/components/saas/search";
  public static final String NN_SEARCH_RESULTS_TABS_CONTAINER = "searchresults-tabs";
  public static final String I18N_KEY_SEARCH_BUTTON_LABEL = "com.valtech.aem.saas.core.search.submit.button.label";
  public static final int DEFAULT_AUTOCOMPLETE_THRESHOLD = 3;

  @Self
  private SlingHttpServletRequest request;

  @OSGiService
  protected ModelFactory modelFactory;

  @OSGiService
  private I18nProvider i18nProvider;

  @Getter
  @ValueMapValue
  @Default(intValues = SearchResultsImpl.DEFAULT_RESULTS_PER_PAGE)
  private int resultsPerPage;

  @Getter
  @JsonInclude(Include.NON_EMPTY)
  @ValueMapValue
  private String searchFieldPlaceholderText;

  @JsonInclude(Include.NON_EMPTY)
  private String searchButtonText;

  @JsonInclude(Include.NON_EMPTY)
  private String loadMoreButtonText;

  @JsonInclude(Include.NON_NULL)
  @Getter
  @ChildResource
  private List<Filter> filters;

  @Getter
  @ValueMapValue
  @Default(intValues = DEFAULT_AUTOCOMPLETE_THRESHOLD)
  private int autocompleteTriggerThreshold;

  @Getter
  @ValueMapValue
  private boolean bestBetsEnabled;

  @Getter
  @ValueMapValue
  private boolean autoSuggestEnabled;

  @Getter
  @JsonInclude(Include.NON_EMPTY)
  private String term;

  private I18n i18n;

  @NonNull
  @Override
  public Map<String, ? extends ComponentExporter> getExportedItems() {
    return getSearchResultsExportedItems();
  }

  private Map<String, SearchResults> getSearchResultsExportedItems() {
    return Optional.ofNullable(request.getResource().getChild(NN_SEARCH_RESULTS_TABS_CONTAINER))
        .map(ResourceConsumer::new)
        .map(ResourceConsumer::getDirectChildren)
        .orElse(Stream.empty())
        .collect(Collectors.toMap(Resource::getName,
            resource -> modelFactory.getModelFromWrappedRequest(request, resource, SearchResults.class)));
  }

  @NonNull
  @Override
  public String[] getExportedItemsOrder() {
    Map<String, ? extends ComponentExporter> models = getExportedItems();
    return models.isEmpty()
        ? ArrayUtils.EMPTY_STRING_ARRAY
        : models.keySet().toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  @NonNull
  @Override
  public String getExportedType() {
    return request.getResource().getResourceType();
  }

  @PostConstruct
  private void init() {
    if (request != null) {
      i18n = i18nProvider.getI18n(request);
      RequestConsumer requestConsumer = new RequestConsumer(request);
      requestConsumer.getParameter(SearchResultsImpl.SEARCH_TERM).ifPresent(t -> term = t);
    }
  }

  @Override
  public String getSearchButtonText() {
    return i18n.get(I18N_KEY_SEARCH_BUTTON_LABEL);
  }

  @Override
  public String getLoadMoreButtonText() {
    return i18n.get(SearchResultsImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL);
  }
}
