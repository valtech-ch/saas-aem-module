package com.valtech.aem.saas.core.fulltextsearch;


import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchRedirectModel;
import com.valtech.aem.saas.api.resource.PathTransformer;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.util.LoggedOptional;
import com.valtech.aem.saas.core.util.ResourceUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.apache.sling.models.factory.ModelFactory;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.stream.Stream;

import static com.valtech.aem.saas.core.fulltextsearch.SearchRedirectModelImpl.RESOURCE_TYPE;

/**
 * Search redirect component sling model that handles component's rendering.
 */
@Slf4j
@Model(adaptables = SlingHttpServletRequest.class,
       adapters = {SearchRedirectModel.class, ComponentExporter.class},
       defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
       resourceType = RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
          extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class SearchRedirectModelImpl implements SearchRedirectModel, ComponentExporter {

    public static final String RESOURCE_TYPE = "saas-aem-module/components/searchredirect";
    private static final String HTML_EXTENSION = ".html";

    @Getter
    @ValueMapValue
    private String searchFieldPlaceholderText;

    @Getter
    @ValueMapValue(name = JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY)
    private String exportedType;

    @Getter
    private String searchUrl;

    @Getter
    private String autocompleteUrl;

    @JsonIgnore
    @Getter
    private String configJson;

    @ValueMapValue
    private String searchPagePath;

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private PathTransformer pathTransformer;

    @OSGiService
    private ModelFactory modelFactory;

    @ScriptVariable
    private Page currentPage;

    private SearchModel searchModel;

    private Resource searchPageResource;

    @PostConstruct
    private void init() {
        getSearchPageResource().ifPresent(r -> searchPageResource = r);
        getSearchModel().ifPresent(search -> searchModel = search);
        retrieveAutocompleteUrl().ifPresent(url -> autocompleteUrl = url);
        resolveSearchFieldPlaceholderText().ifPresent(s -> searchFieldPlaceholderText = s);
        createSearchPageUrl().ifPresent(s -> searchUrl = s);
        configJson = getSearchConfigJson();
    }

    @Override
    public boolean render() {
        return Optional.ofNullable(currentPage)
                       .map(Page::getContentResource)
                       .map(r -> r.adaptTo(ResourceWrapper.class))
                       .map(ResourceWrapper::getDescendents)
                       .orElse(Stream.empty())
                       .noneMatch(r -> r.isResourceType(SearchModelImpl.RESOURCE_TYPE));
    }

    private Optional<String> createSearchPageUrl() {
        return Optional.ofNullable(searchPageResource)
                       .map(r -> pathTransformer.map(request, r.getPath()))
                       .map(s -> s + HTML_EXTENSION);
    }

    private String getSearchConfigJson() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize search config to json.", e);
        }
        return StringUtils.EMPTY;
    }

    private Optional<String> retrieveAutocompleteUrl() {
        return Optional.ofNullable(searchModel).map(SearchModel::getAutocompleteUrl);
    }

    private Optional<SearchModel> getSearchModel() {
        return LoggedOptional.of(searchPageResource, logger -> logger.error("Search page resource is not existing."))
                             .map(r -> r.adaptTo(ResourceWrapper.class))
                             .map(ResourceWrapper::getDescendents)
                             .orElse(Stream.empty())
                             .filter(r -> resourceResolver.isResourceType(r, SearchModelImpl.RESOURCE_TYPE))
                             .findFirst()
                             .flatMap(search -> LoggedOptional.of(search,
                                                                  logger -> logger.error("Search component not found.")))
                             .map(r -> modelFactory.getModelFromWrappedRequest(request, r, SearchModel.class));
    }

    private Optional<String> resolveSearchFieldPlaceholderText() {
        if (StringUtils.isNotBlank(searchFieldPlaceholderText)) {
            return Optional.of(searchFieldPlaceholderText);
        }
        return Optional.ofNullable(searchModel)
                       .map(SearchModel::getSearchFieldPlaceholderText);
    }

    private Optional<Resource> getSearchPageResource() {
        return Optional.ofNullable(searchPagePath)
                       .filter(StringUtils::isNotBlank)
                       .flatMap(s -> LoggedOptional.of(s,
                                                       logger -> logger.error("Search page path is not configured.")))
                       .map(pPath -> resourceResolver.getResource(pPath));
    }

    @Override
    public String getId() {
        return ResourceUtil.generateId("saas", resource.getPath());
    }
}
