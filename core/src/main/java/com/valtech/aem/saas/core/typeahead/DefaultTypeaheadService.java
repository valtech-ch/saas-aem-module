package com.valtech.aem.saas.core.typeahead;

import com.valtech.aem.saas.api.typeahead.TypeaheadConfigurationService;
import com.valtech.aem.saas.api.typeahead.TypeaheadConsumerService;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.indexing.DefaultIndexUpdateService.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
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
  public TypeaheadConsumerService getTypeaheadConsumerService(String index) {
    if (StringUtils.isBlank(index)) {
      throw new IllegalArgumentException("Must specify a SaaS client index.");
    }
    return IndexTypeaheadConsumerService.builder()
        .searchRequestExecutorService(searchRequestExecutorService)
        .apiUrl(getApiUrl(index))
        .allowedFilterFields(getAllowedFilterFields())
        .build();
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
