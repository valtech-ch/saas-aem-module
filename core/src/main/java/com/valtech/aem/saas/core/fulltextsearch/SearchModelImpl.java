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
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.autocomplete.AutocompleteServlet;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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
import org.apache.http.client.utils.URIBuilder;
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
  public static final int AUTOCOMPLETE_THRESHOLD = 3;
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
  private Set<FilterModel> effectiveFilters;

  @Getter
  private String searchButtonText;

  @Getter
  private String loadMoreButtonText;

  @Getter
  private List<String> searchTabs;

  @JsonIgnore
  @Getter
  private String configJson;

  @Getter
  @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
  private String exportedType;

  @ValueMapValue
  private String language;

  @Self
  private SlingHttpServletRequest request;

  @SlingObject
  private Resource resource;

  @OSGiService
  private PathTransformer pathTransformer;

  @OSGiService
  private I18nProvider i18nProvider;

  @PostConstruct
  private void init() {
    I18n i18n = i18nProvider.getI18n(getLocale());
    effectiveFilters = getEffectiveFilters(resource);
    searchFieldPlaceholderText = StringUtils.isNotBlank(searchFieldPlaceholderText)
        ? searchFieldPlaceholderText
        : i18n.get(I18N_SEARCH_INPUT_PLACEHOLDER);
    searchButtonText = i18n.get(I18N_KEY_SEARCH_BUTTON_LABEL);
    loadMoreButtonText = i18n.get(SearchTabModelImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL);
    if (request != null) {
      searchTabs = getSearchTabs(resource);
    }
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

  @Override
  public int getAutocompleteTriggerThreshold() {
    return AUTOCOMPLETE_THRESHOLD;
  }

  private boolean isAutocompleteRequest() {
    return Optional.ofNullable(request)
        .map(r -> r.adaptTo(RequestWrapper.class))
        .map(RequestWrapper::getSelectors)
        .orElse(Collections.emptyList())
        .contains(AutocompleteServlet.AUTOCOMPLETE_SELECTOR);
  }

  private List<String> getSearchTabs(Resource searchResource) {
    if (isAutocompleteRequest()) {
      return Collections.emptyList();
    }
    return Optional.ofNullable(searchResource.getChild(NODE_NAME_SEARCH_TABS_CONTAINER))
        .map(r -> r.adaptTo(ResourceWrapper.class))
        .map(ResourceWrapper::getDirectChildren)
        .orElse(Stream.empty())
        .map(this::getSearchTabUrl)
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
  }

  private String getSearchConfigJson() {
    try {
      return new ObjectMapper().writeValueAsString(this);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize search config to json.", e);
    }
    return StringUtils.EMPTY;
  }

  private Set<FilterModel> getEffectiveFilters(Resource resource) {
    Set<FilterModel> distinctFilters = new HashSet<>(getCaFilters(resource));
    if (filters != null) {
      distinctFilters.addAll(filters);
    }
    return distinctFilters;
  }

  private List<FilterModel> getCaFilters(Resource resource) {
    return Optional.ofNullable(resource.adaptTo(ConfigurationBuilder.class))
        .map(configurationBuilder -> configurationBuilder.as(SearchConfiguration.class))
        .map(SearchConfiguration::searchFilters)
        .map(Arrays::stream)
        .orElse(Stream.empty())
        .map(searchFilterConfiguration -> new FilterModelImpl(searchFilterConfiguration.name(),
            searchFilterConfiguration.value()))
        .collect(Collectors.toList());
  }

  private String getSearchTabUrl(@NonNull Resource searchTab) {
    try {
      return new URIBuilder(String.format("%s.%s.%s",
          pathTransformer.map(request, searchTab.getPath()),
          ExporterConstants.SLING_MODEL_SELECTOR,
          ExporterConstants.SLING_MODEL_EXTENSION))
          .setCustomQuery(request.getQueryString())
          .toString();
    } catch (URISyntaxException e) {
      log.error("Failed to create search prepared url to search tab resource.");
    }
    return null;
  }

  private Locale getLocale() {
    return Optional.ofNullable(resource.adaptTo(ResourceWrapper.class)).map(ResourceWrapper::getLocale)
        .orElse(Locale.getDefault());
  }
}
