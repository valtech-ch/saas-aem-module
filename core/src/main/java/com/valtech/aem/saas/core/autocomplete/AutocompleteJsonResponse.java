package com.valtech.aem.saas.core.autocomplete;

import static com.valtech.aem.saas.core.fulltextsearch.SearchImpl.RESOURCE_TYPE;

import com.day.cq.wcm.api.Page;
import com.google.gson.Gson;
import com.valtech.aem.saas.api.autocomplete.AutocompleteResponse;
import com.valtech.aem.saas.api.caconfig.SearchConfiguration;
import com.valtech.aem.saas.api.typeahead.TypeaheadConsumerService;
import com.valtech.aem.saas.api.typeahead.TypeaheadPayload;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.common.request.RequestWrapper;
import com.valtech.aem.saas.core.common.response.JsonResponseFlusher;
import com.valtech.aem.saas.core.fulltextsearch.SearchResultsImpl;
import com.valtech.aem.saas.core.typeahead.DefaultTypeaheadPayload;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.caconfig.ConfigurationBuilder;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = AutocompleteResponse.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
    resourceType = RESOURCE_TYPE)
public class AutocompleteJsonResponse implements AutocompleteResponse {

  public static final String QUERY_PARAM_LANGUAGE = "language";
  public static final String DEFAULT_LANGUAGE = "en";

  @Self
  private SlingHttpServletRequest request;

  @SlingObject
  private SlingHttpServletResponse response;

  @ScriptVariable
  private Page currentPage;

  @OSGiService
  private TypeaheadService typeaheadService;

  @PostConstruct
  private void init() {
    if (!response.isCommitted()) {
      Optional.ofNullable(request.adaptTo(RequestWrapper.class))
          .ifPresent(requestWrapper ->
              requestWrapper.getParameter(SearchResultsImpl.SEARCH_TERM).ifPresent(text -> {
                SearchConfiguration searchConfiguration = request.getResource().adaptTo(ConfigurationBuilder.class)
                    .as(SearchConfiguration.class);
                TypeaheadPayload payload = DefaultTypeaheadPayload.builder()
                    .text(text)
                    .language(getLanguage(requestWrapper))
                    .build();
                TypeaheadConsumerService typeaheadConsumerService =
                    typeaheadService.getTypeaheadConsumerService(searchConfiguration.index());
                List<String> results = typeaheadConsumerService.getResults(payload);
                new JsonResponseFlusher(response).flush(printWriter -> new Gson().toJson(results, printWriter));
              }));
    }
  }

  private String getLanguage(RequestWrapper requestWrapper) {
    return requestWrapper.getParameter(QUERY_PARAM_LANGUAGE)
        .orElseGet(() -> currentPage.getLanguage().getLanguage());
  }

}
