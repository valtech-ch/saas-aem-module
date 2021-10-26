package com.valtech.aem.saas.core.fulltextsearch;

import static com.valtech.aem.saas.core.fulltextsearch.SearchImpl.RESOURCE_TYPE;

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
import com.valtech.aem.saas.api.fulltextsearch.Filter;
import com.valtech.aem.saas.api.fulltextsearch.Search;
import com.valtech.aem.saas.api.resource.PathTransformer;
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
import org.apache.sling.models.annotations.ExporterOption;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Search component sling model that handles component's rendering.
 */
@Slf4j
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

  public static final String RESOURCE_TYPE = "saas-aem-module/components/search";
  public static final String NODE_NAME_SEARCH_TABS_CONTAINER = "search-tabs";
  public static final String I18N_KEY_SEARCH_BUTTON_LABEL = "com.valtech.aem.saas.core.search.submit.button.label";
  public static final int AUTOCOMPLETE_THRESHOLD = 3;
  public static final String I18N_SEARCH_INPUT_PLACEHOLDER = "com.valtech.aem.saas.core.search.input.placeholder.text";

  @Self
  private SlingHttpServletRequest request;

  @Self
  private Resource resource;

  @OSGiService
  private PathTransformer pathTransformer;

  @OSGiService
  protected I18nProvider i18nProvider;

  @JsonIgnore
  @Getter
  @ValueMapValue
  @Default(intValues = SearchTabImpl.DEFAULT_RESULTS_PER_PAGE)
  private int resultsPerPage;

  @Getter
  @JsonInclude(Include.NON_EMPTY)
  @ValueMapValue
  private String title;

  @Getter
  @JsonInclude(Include.NON_EMPTY)
  @ValueMapValue
  private String searchFieldPlaceholderText;

  @ChildResource
  private List<Filter> filters;

  private Set<Filter> mergedFilters;

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
  @JsonIgnore
  private String term;

  @Getter
  @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
  private String exportedType;

  @PostConstruct
  private void init() {
    if (request != null) {
      Optional.ofNullable(request.adaptTo(RequestWrapper.class))
          .ifPresent(requestWrapper -> {
            requestWrapper.getParameter(SearchTabImpl.SEARCH_TERM).ifPresent(t -> term = t);
            initSearch(request.getResource(), requestWrapper.getLocale());
          });
    } else if (resource != null) {
      Optional.ofNullable(resource.adaptTo(ResourceWrapper.class))
          .ifPresent(resourceWrapper -> initSearch(resource, resourceWrapper.getLocale()));
    }
  }

  private void initSearch(Resource resource, Locale locale) {
    I18n i18n = i18nProvider.getI18n(locale);
    mergedFilters = getMergedFilters(resource);
    searchFieldPlaceholderText = StringUtils.isNotBlank(searchFieldPlaceholderText) ? searchFieldPlaceholderText
        : i18n.get(I18N_SEARCH_INPUT_PLACEHOLDER);
    searchButtonText = getSearchButtonText(i18n);
    loadMoreButtonText = getLoadMoreButtonText(i18n);
    if (request != null) {
      searchTabs = getSearchTabs(resource);
    }
    configJson = getSearchConfigJson();
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

  @JsonIgnore
  @Override
  public Set<Filter> getFilters() {
    return mergedFilters;
  }

  private String getSearchButtonText(I18n i18n) {
    return Optional.ofNullable(i18n)
        .map(t -> t.get(I18N_KEY_SEARCH_BUTTON_LABEL))
        .orElse(StringUtils.EMPTY);
  }

  private String getLoadMoreButtonText(I18n i18n) {
    return Optional.ofNullable(i18n)
        .map(t -> t.get(SearchTabImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL))
        .orElse(StringUtils.EMPTY);
  }

  @Override
  public int getAutocompleteTriggerThreshold() {
    return AUTOCOMPLETE_THRESHOLD;
  }

  private List<String> getSearchTabs(Resource searchResource) {
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

  private Set<Filter> getMergedFilters(Resource resource) {
    Set<Filter> distinctFilters = new HashSet<>(getCaFilters(resource));
    if (filters != null) {
      distinctFilters.addAll(filters);
    }
    return distinctFilters;
  }

  private List<Filter> getCaFilters(Resource resource) {
    return Optional.ofNullable(resource.adaptTo(ConfigurationBuilder.class))
        .map(configurationBuilder -> configurationBuilder.as(SearchConfiguration.class))
        .map(SearchConfiguration::searchFilters)
        .map(Arrays::stream)
        .orElse(Stream.empty())
        .map(searchFilterConfiguration -> new FilterImpl(searchFilterConfiguration.name(),
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
}
