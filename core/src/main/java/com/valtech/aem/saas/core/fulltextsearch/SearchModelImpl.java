package com.valtech.aem.saas.core.fulltextsearch;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Container;
import com.adobe.cq.wcm.core.components.models.datalayer.ComponentData;
import com.adobe.cq.wcm.core.components.models.datalayer.builder.DataLayerBuilder;
import com.adobe.cq.wcm.core.components.util.ComponentUtils;
import com.day.cq.i18n.I18n;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchPingService;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.api.query.Filter;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.autocomplete.AutocompleteServlet;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.i18n.I18nProvider;
import com.valtech.aem.saas.core.util.ResourceUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.valtech.aem.saas.core.fulltextsearch.SearchModelImpl.RESOURCE_TYPE;

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
public class SearchModelImpl implements SearchModel, Container {

    public static final String RESOURCE_TYPE = "saas-aem-module/components/search";
    public static final String NODE_NAME_SEARCH_TABS_CONTAINER = "search-tabs";
    public static final String I18N_KEY_SEARCH_BUTTON_LABEL = "com.valtech.aem.saas.core.search.submit.button.label";
    public static final String I18N_SEARCH_INPUT_PLACEHOLDER =
            "com.valtech.aem.saas.core.search.input.placeholder.text";
    public static final String I18N_SEARCH_SUGGESTION_TEXT = "com.valtech.aem.saas.core.search.suggestion.text";
    public static final String I18N_SEARCH_NO_RESULTS_TEXT = "com.valtech.aem.saas.core.search.noResults.text";
    public static final String I18N_SEARCH_CA_CONFIGURATION_FAILED_TO_RESOLVE =
            "com.valtech.aem.saas.core.search.caConfiguration.failedToResolveConfigurationMessage";
    public static final String I18N_SEARCH_CONNECTION_FAILED_FURTHER_ACTION_CHECK_LOG_FILES =
            "com.valtech.aem.saas.core.search.apiConnectionFail.furtherAction.checkLogFiles.text";
    public static final String I18N_SEARCH_CONNECTION_FAILED_FURTHER_ACTION_CHECK_OSGI_CONFIGURATION =
            "com.valtech.aem.saas.core.search.apiConnectionFail.furtherAction.checkOsgiConfigurations.text";

    @JsonInclude(Include.NON_EMPTY)
    @Getter
    @ValueMapValue
    private String title;

    @JsonIgnore
    @Getter
    @ValueMapValue
    @Default(intValues = SearchTabModelImpl.DEFAULT_RESULTS_PER_PAGE)
    private int resultsPerPage;

    @JsonInclude(Include.NON_EMPTY)
    @Getter
    @ValueMapValue
    private String searchFieldPlaceholderText;

    @Getter
    @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
    private String exportedType;

    @Getter
    private String searchButtonText;

    @Getter
    private String loadMoreButtonText;

    @Getter
    private List<SearchTabModel> searchTabs;

    @Getter
    private String autocompleteUrl;

    @Getter
    private String autoSuggestText;

    @Getter
    private String noResultsText;

    @Getter
    private ConnectionFailedAlert connectionFailedAlert;

    @Getter
    private int autocompleteTriggerThreshold;

    @JsonIgnore
    @Getter
    private String configJson;

    @JsonIgnore
    @Getter
    private Set<Filter> filters;

    @ChildResource(name = "filters")
    private List<FilterConfigurationModel> configuredFilters;

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

    @OSGiService
    private FulltextSearchPingService fulltextSearchPingService;

    private I18n i18n;

    private SearchCAConfigurationModel searchCAConfigurationModel;

    @Override
    public ComponentData getData() {
        if (ComponentUtils.isDataLayerEnabled(this.resource)) {
            return DataLayerBuilder.forContainer()
                                   .withId(this::getId)
                                   .withTitle(this::getTitle)
                                   .withType(this::getExportedType)
                                   .build();

        }
        return null;
    }


    @PostConstruct
    private void init() {
        searchCAConfigurationModel = resource.adaptTo(SearchCAConfigurationModel.class);
        createAutocompleteUrl().ifPresent(url -> autocompleteUrl = url);
        i18n = i18nProvider.getI18n(getLocale());
        connectionFailedAlert = resolveConnectionFailedAlert();
        getAutocompleteThreshold().ifPresent(threshold -> autocompleteTriggerThreshold = threshold);
        autoSuggestText = i18n.get(I18N_SEARCH_SUGGESTION_TEXT);
        noResultsText = i18n.get(I18N_SEARCH_NO_RESULTS_TEXT);
        filters = getConfiguredFilters();
        searchFieldPlaceholderText = StringUtils.isNotBlank(searchFieldPlaceholderText)
                ? searchFieldPlaceholderText
                : i18n.get(I18N_SEARCH_INPUT_PLACEHOLDER);
        searchButtonText = i18n.get(I18N_KEY_SEARCH_BUTTON_LABEL);
        loadMoreButtonText = i18n.get(SearchTabModelImpl.I18N_KEY_LOAD_MORE_BUTTON_LABEL);
        searchTabs = getSearchTabList();
        configJson = getSearchConfigJson();
    }

    @JsonIgnore
    public String getLanguage() {
        return StringUtils.isNotBlank(language)
                ? language
                : getLocale().getLanguage();
    }

    @Override
    public String getId() {
        return ResourceUtil.generateId("saas", resource.getPath());
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
        if (isResolvedFromRequest() && !isResourceOverriddenRequest()) {
            return searchTabResources.stream()
                                     .map(r -> modelFactory.getModelFromWrappedRequest(request,
                                                                                       r,
                                                                                       SearchTabModel.class))
                                     .filter(Objects::nonNull)
                                     .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Optional<String> createAutocompleteUrl() {
        if (Optional.ofNullable(searchCAConfigurationModel)
                    .filter(SearchCAConfigurationModel::isAutocompleteEnabled)
                    .isPresent()) {
            return Optional.ofNullable(request)
                           .map(r -> pathTransformer.map(r, resource.getPath()))
                           .map(url -> String.format("%s.%s.%s",
                                                     url,
                                                     AutocompleteServlet.AUTOCOMPLETE_SELECTOR,
                                                     AutocompleteServlet.EXTENSION_JSON));
        }
        log.info("Autocomplete is not enabled. To enable it, please check context aware SearchConfiguration.");
        return Optional.empty();
    }

    private String getSearchConfigJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize search config to json.", e);
        }
        return StringUtils.EMPTY;
    }

    private Set<Filter> getConfiguredFilters() {
        return Optional.ofNullable(configuredFilters)
                       .map(List::stream)
                       .orElse(Stream.empty())
                       .filter(FilterConfiguration::isValid)
                       .map(FilterConfiguration::getFilter)
                       .collect(Collectors.toSet());
    }

    private Locale getLocale() {
        return Optional.ofNullable(resource.adaptTo(ResourceWrapper.class)).map(ResourceWrapper::getLocale)
                       .orElse(Locale.getDefault());
    }

    private ConnectionFailedAlert resolveConnectionFailedAlert() {
        if (isResolvedFromRequest() && !isResourceOverriddenRequest()) {
            if (searchCAConfigurationModel == null) {
                log.error("Can not resolve context aware search configuration model.");
                return new ConnectionFailedAlert(AlertVariant.WARNING,
                                                 Collections.singletonList(i18n.get(
                                                         I18N_SEARCH_CA_CONFIGURATION_FAILED_TO_RESOLVE,
                                                         null,
                                                         resource.getPath())));
            }
            try {
                boolean pingSuccess = fulltextSearchPingService.ping(searchCAConfigurationModel);
                if (!pingSuccess) {
                    return new ConnectionFailedAlert(AlertVariant.ERROR,
                                                     Arrays.asList(i18n.get(
                                                                           I18N_SEARCH_CONNECTION_FAILED_FURTHER_ACTION_CHECK_OSGI_CONFIGURATION),
                                                                   i18n.get(
                                                                           I18N_SEARCH_CONNECTION_FAILED_FURTHER_ACTION_CHECK_LOG_FILES)));
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return new ConnectionFailedAlert(AlertVariant.WARNING, Collections.singletonList(e.getMessage()));
            }
        }
        return null;
    }

    private Optional<Integer> getAutocompleteThreshold() {
        return Optional.ofNullable(searchCAConfigurationModel)
                       .map(SearchCAConfigurationModel::getAutocompleteThreshold);
    }

    private boolean isResourceOverriddenRequest() {
        return !StringUtils.equals(request.getRequestPathInfo().getResourcePath(), resource.getPath());
    }

    private boolean isResolvedFromRequest() {
        return request != null;
    }
}
