package com.valtech.aem.saas.core.typeahead;

import com.valtech.aem.saas.api.typeahead.TypeaheadConfigurationService;
import com.valtech.aem.saas.api.typeahead.TypeaheadPayload;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.request.SearchRequestGet;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.http.response.TypeaheadDataExtractionStrategy;
import com.valtech.aem.saas.core.indexing.DefaultIndexUpdateService.Configuration;
import com.valtech.aem.saas.core.query.DefaultLanguageQuery;
import com.valtech.aem.saas.core.query.FiltersQuery;
import com.valtech.aem.saas.core.query.GetQueryStringConstructor;
import com.valtech.aem.saas.core.query.TypeaheadTextQuery;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Slf4j
@Component(name = "Search as a Service - Typeahead Service",
    service = {TypeaheadService.class, TypeaheadConfigurationService.class})
@Designate(ocd = Configuration.class)
public class DefaultTypeaheadService implements TypeaheadService, TypeaheadConfigurationService {

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  @Reference
  private SearchRequestExecutorService searchRequestExecutorService;

  private Configuration configuration;

  @Override
  public List<String> getResults(@NonNull String index, @NonNull TypeaheadPayload typeaheadPayload) {
    if (StringUtils.isBlank(typeaheadPayload.getText())) {
      throw new IllegalArgumentException("Typeahead payload should contain a search text.");
    }
    if (StringUtils.isBlank(typeaheadPayload.getLanguage())) {
      throw new IllegalArgumentException("Typeahead payload should contain a language.");
    }
    validateFilterFields(typeaheadPayload);

    SearchRequestGet searchRequestGet = new SearchRequestGet(getApiUrl(index) + getQueryString(typeaheadPayload));
    return searchRequestExecutorService.execute(searchRequestGet)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new TypeaheadDataExtractionStrategy(typeaheadPayload.getLanguage())))
        .orElse(Collections.emptyList());
  }

  private String getQueryString(@NonNull TypeaheadPayload typeaheadPayload) {
    GetQueryStringConstructor.GetQueryStringConstructorBuilder builder =
        GetQueryStringConstructor.builder()
            .query(new TypeaheadTextQuery(typeaheadPayload.getText()))
            .query(new DefaultLanguageQuery(typeaheadPayload.getLanguage()));
    if (MapUtils.isNotEmpty(typeaheadPayload.getFilterEntries())) {
      FiltersQuery.FiltersQueryBuilder filtersQueryBuilder = FiltersQuery.builder();
      typeaheadPayload.getFilterEntries().forEach(filtersQueryBuilder::filterEntry);
      builder.query(filtersQueryBuilder.build());
    }
    return builder.build().getQueryString();
  }


  private void validateFilterFields(@NonNull TypeaheadPayload typeaheadPayload) {
    if (ArrayUtils.isNotEmpty(configuration.typeaheadService_allowedFilterFields())) {
      List<String> forbiddenFilterFields = typeaheadPayload.getFilterEntries().keySet().stream()
          .filter(field -> !ArrayUtils.contains(configuration.typeaheadService_allowedFilterFields(), field))
          .collect(Collectors.toList());
      if (CollectionUtils.isNotEmpty(forbiddenFilterFields)) {
        throw new IllegalArgumentException(
            String.format("The following filter field names are not allowed: %s", forbiddenFilterFields));
      }
    }
  }

  @Override
  public List<String> getAllowedFilterFields() {
    return Optional.ofNullable(configuration.typeaheadService_allowedFilterFields())
        .map(Arrays::stream)
        .orElse(Stream.empty())
        .collect(Collectors.toList());
  }

  private String getApiUrl(String index) {
    return String.format("%s%s/%s%s",
        searchServiceConnectionConfigurationService.getBaseUrl(),
        configuration.typeaheadService_apiVersionPath(),
        index,
        configuration.typeaheadService_apiAction());
  }

  @Activate
  @Modified
  private void activate(Configuration configuration) {
    this.configuration = configuration;
  }

  @ObjectClassDefinition(name = "Search as a Service - Typeahead Service Configuration",
      description = "Typeahead Api specific details.")
  public @interface Configuration {

    String DEFAULT_API_ACTION = "/typeahead";
    String DEFAULT_API_VERSION_PATH = "/api/v3";

    String FF_LANGUAGE = "language";
    String FF_DOMAIN = "domain";
    String FF_REPOSITORY_PATH_URL = "repository_path_url";

    @AttributeDefinition(name = "Api version path",
        description = "Path designating the api version",
        type = AttributeType.STRING)
    String typeaheadService_apiVersionPath() default DEFAULT_API_VERSION_PATH;

    @AttributeDefinition(name = "Api action",
        description = "Path designating the action",
        type = AttributeType.STRING)
    String typeaheadService_apiAction() default DEFAULT_API_ACTION;

    @AttributeDefinition(name = "Allowed filter fields",
        description = "List of field names that can be used in filter queries",
        type = AttributeType.STRING)
    String[] typeaheadService_allowedFilterFields() default {
        FF_LANGUAGE,
        FF_DOMAIN,
        FF_REPOSITORY_PATH_URL
    };

  }
}
