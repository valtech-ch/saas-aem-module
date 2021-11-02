package com.valtech.aem.saas.core.autocomplete;

import com.google.gson.Gson;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.SearchModel;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.api.typeahead.dto.DefaultTypeaheadPayloadDTO;
import com.valtech.aem.saas.api.typeahead.dto.TypeaheadPayloadDTO;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.resource.ResourceWrapper;
import com.valtech.aem.saas.core.common.response.JsonResponseCommitter;
import com.valtech.aem.saas.core.fulltextsearch.SearchModelImpl;
import com.valtech.aem.saas.core.fulltextsearch.SearchTabModelImpl;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class)
@SlingServletResourceTypes(resourceTypes = SearchModelImpl.RESOURCE_TYPE,
    selectors = AutocompleteServlet.AUTOCOMPLETE_SELECTOR,
    extensions = AutocompleteServlet.EXTENSION_JSON)
public class AutocompleteServlet extends SlingSafeMethodsServlet {

  public static final String AUTOCOMPLETE_SELECTOR = "autocomplete";
  public static final String EXTENSION_JSON = "json";

  @Reference
  private transient TypeaheadService typeaheadService;

  @Override
  protected void doGet(@NonNull SlingHttpServletRequest request,
      @NonNull SlingHttpServletResponse response) throws ServletException, IOException {

    Optional.ofNullable(request.adaptTo(RequestWrapper.class))
        .ifPresent(requestWrapper ->
            requestWrapper.getParameter(SearchTabModelImpl.SEARCH_TERM).ifPresent(text -> {
              SearchConfiguration searchConfiguration = request.getResource().adaptTo(ConfigurationBuilder.class)
                  .as(SearchConfiguration.class);
              if (StringUtils.isNotBlank(searchConfiguration.index())) {
                TypeaheadPayloadDTO payload = DefaultTypeaheadPayloadDTO.builder()
                    .text(text)
                    .language(getLanguage(request).orElse(StringUtils.EMPTY))
                    .filters(getSearch(request).map(SearchModel::getEffectiveFilters).orElse(Collections.emptySet()))
                    .build();
                List<String> results = typeaheadService.getResults(searchConfiguration.index(), payload);
                new JsonResponseCommitter(response).flush(printWriter -> new Gson().toJson(results, printWriter));
              }
            }));
  }


  private Optional<SearchModel> getSearch(@NonNull SlingHttpServletRequest request) {
    return Optional.ofNullable(request.getResource().adaptTo(SearchModel.class));
  }

  private Optional<String> getLanguage(@NonNull SlingHttpServletRequest request) {
    return Optional.ofNullable(request.getResource().adaptTo(ResourceWrapper.class)).map(ResourceWrapper::getLocale)
        .map(Locale::getLanguage);
  }
}
