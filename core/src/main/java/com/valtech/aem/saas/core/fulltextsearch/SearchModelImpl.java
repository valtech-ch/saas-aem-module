package com.valtech.aem.saas.core.fulltextsearch;

import static com.valtech.aem.saas.core.fulltextsearch.SearchModelImpl.RESOURCE_TYPE;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.i18n.I18n;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FilterModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.query.SimpleFilter;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.autocomplete.AutocompleteServlet;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.factory.ModelFactory;

/**
 * Search component sling model that handles component's rendering.
 */
@Slf4j
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class},
    adapters = {SearchModel.class, ComponentExporter.class, ContainerExporter.class},
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
    extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchModelImpl implements SearchModel {

  public static final String RESOURCE_TYPE = "saas-aem-module/components/search";
  public static final String NODE_NAME_SEARCH_TABS_CONTAINER = "search-tabs";
  public static final String I18N_KEY_SEARCH_BUTTON_LABEL = "com.valtech.aem.saas.core.search.submit.button.label";
  public static final String I18N_SEARCH_INPUT_PLACEHOLDER = "com.valtech.aem.saas.core.search.input.placeholder.text";


  @Getter
  @JsonInclude(Include.NON_EMPTY)
  @ValueMapValue
  private String title;

  @JsonIgnore
  @Getter
  @ChildResource
  private List<FilterModel> filters;

  @JsonIgnore
  @Getter
  @ValueMapValue
  @Default(intValues = SearchTabModelImpl.DEFAULT_RESULTS_PER_PAGE)
  private int resultsPerPage;

  @Getter
  @JsonInclude(Include.NON_EMPTY)
  @ValueMapValue
  private String searchFieldPlaceholderText;

  @JsonIgnore
  @Getter
  private Set<Filter> effectiveFilters;

  @Getter
  private String searchButtonText;

  @Getter
  private String loadMoreButtonText;

  @Getter
  private List<SearchTabModel> searchTabs;

  @JsonIgnore
  @Getter
  private String configJson;

  @Getter
  @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
  private String exportedType;

  @Getter
  private String autosuggestUrl;

  @ChildResource(name = NODE_NAME_SEARCH_TABS_CONTAINER)
  private List<Resource> searchTabResources;

  @ValueMapValue
  private String language;

  @Self
  private SlingHttpServletRequest request;

  @SlingObject
  private Resource resource;

  @OSGiService
  private I18nProvider i18nProvider;

  @OSGiService
  private ModelFactory modelFactory;

  @OSGiService
  private PathTransformer pathTransformer;

  @PostConstruct
  private void init() {
    createAutosuggestUrl().ifPresent(url -> autosuggestUrl = url);
    I18n i18n = i18nProvider.getI18n(getLocale());
    effectiveFilters = getEffectiveFilters(resource);
    searchFieldPlaceholderText = StringUtils.isNotBlank(searchFieldPlaceholderText)
        ? searchFieldPlaceholderText
        : i18n.get(I18N_SEARCH_INPUT_PLACEHOLDER);
    searchButtonText = i18n.get(I18N_KEY_SEARCH_BUTTON_LABEL);
    loadMoreButtonText = i18n.get(SearchTabModelImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL);
    searchTabs = getSearchTabList();
    configJson = getSearchConfigJson();
  }

  @JsonIgnore
  @Override
  public String getLanguage() {
    return StringUtils.isNotBlank(language) ? language : getLocale().getLanguage();
  }

  @NonNull
  @Override
  public Map<String, ? extends ComponentExporter> getExportedItems() {
    return Collections.emptyMap();
  }

  @Override
  public String @NonNull [] getExportedItemsOrder() {
    Map<String, ? extends ComponentExporter> models = getExportedItems();
    return models.isEmpty()
        ? ArrayUtils.EMPTY_STRING_ARRAY
        : models.keySet().toArray(ArrayUtils.EMPTY_STRING_ARRAY);
  }

  private List<SearchTabModel> getSearchTabList() {
    if (request != null) {
      return searchTabResources.stream()
          .map(r -> modelFactory.getModelFromWrappedRequest(request, r, SearchTabModel.class))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  private Optional<String> createAutosuggestUrl() {
    return Optional.ofNullable(request).map(r -> pathTransformer.map(r, resource.getPath()))
        .map(url -> String.format("%s.%s.%s",
            url,
            AutocompleteServlet.AUTOCOMPLETE_SELECTOR,
            AutocompleteServlet.EXTENSION_JSON));
  }

  private String getSearchConfigJson() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize search config to json.", e);
    }
    return StringUtils.EMPTY;
  }

  private Set<Filter> getEffectiveFilters(Resource resource) {
    Set<Filter> effectiveFiltersList = getCaFilters(resource);
    effectiveFiltersList.addAll(getConfiguredFilters());
    return effectiveFiltersList;
  }

  private Set<Filter> getConfiguredFilters() {
    return Optional.ofNullable(filters)
        .map(List::stream)
        .orElse(Stream.empty())
        .map(FilterModel::getFilter)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }

  private Set<Filter> getCaFilters(Resource resource) {
    return Optional.ofNullable(resource.adaptTo(ConfigurationBuilder.class))
        .map(configurationBuilder -> configurationBuilder.as(SearchConfiguration.class))
        .map(SearchConfiguration::searchFilters)
        .map(Arrays::stream)
        .orElse(Stream.empty())
        .map(searchFilterConfiguration -> new SimpleFilter(searchFilterConfiguration.name(),
            searchFilterConfiguration.value()))
        .collect(Collectors.toSet());
  }

  private Locale getLocale() {
    return Optional.ofNullable(resource.adaptTo(ResourceWrapper.class)).map(ResourceWrapper::getLocale)
        .orElse(Locale.getDefault());
  }
}
