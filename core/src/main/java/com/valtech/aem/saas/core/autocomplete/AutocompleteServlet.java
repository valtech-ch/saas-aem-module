package com.valtech.aem.saas.core.autocomplete;

import com.google.gson.Gson;
import com.valtech.aem.saas.api.autocomplete.AutocompleteService;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.fulltextsearch.SearchTabModel;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.response.JsonResponseCommitter;
import com.valtech.aem.saas.core.fulltextsearch.SearchModelImpl;
import lombok.NonNull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = SearchModelImpl.RESOURCE_TYPE,
                           selectors = AutocompleteServlet.AUTOCOMPLETE_SELECTOR,
                           extensions = AutocompleteServlet.EXTENSION_JSON)
public class AutocompleteServlet extends SlingSafeMethodsServlet {

    public static final String AUTOCOMPLETE_SELECTOR = "autocomplete";
    public static final String EXTENSION_JSON = "json";

    @Reference
    private transient AutocompleteService autocompleteService;

    @Override
    protected void doGet(@NonNull SlingHttpServletRequest request,
                         @NonNull SlingHttpServletResponse response) throws ServletException, IOException {
        RequestWrapper requestWrapper = request.adaptTo(RequestWrapper.class);
        if (requestWrapper == null) {
            throw new IllegalArgumentException("Can not adapt the request to RequestWrapper sling model.");
        }
        String searchTerm = requestWrapper.getParameter(SearchTabModel.QUERY_PARAM_SEARCH_TERM)
                                          .orElseThrow(() -> new IllegalArgumentException("Search term not specified" +
                                                                                                  "."));
        SearchModel searchModel = getSearch(request.getResource()).orElseThrow(() -> new IllegalStateException(
                "Can not resolve search model."));
        SearchCAConfigurationModel searchCAConfigurationModel =
                Optional.ofNullable(request.getResource().adaptTo(SearchCAConfigurationModel.class))
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Could not access search CA configurations from current resource."));
        List<String> results = autocompleteService.getResults(searchCAConfigurationModel,
                                                              searchTerm,
                                                              searchModel.getLanguage(),
                                                              searchModel.getFilters(),
                                                              searchModel.isDisableContextFilters());
        new JsonResponseCommitter(response).flush(printWriter -> new Gson().toJson(results, printWriter));
    }

    private Optional<SearchModel> getSearch(@NonNull Resource resource) {
        return Optional.ofNullable(resource.adaptTo(SearchModel.class));
    }

}
