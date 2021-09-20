package com.valtech.aem.saas.core.typeahead;

import com.valtech.aem.saas.api.typeahead.TypeaheadConfigurationService;
import com.valtech.aem.saas.api.typeahead.TypeaheadConsumerService;
import com.valtech.aem.saas.api.typeahead.TypeaheadService;
import com.valtech.aem.saas.core.common.saas.SaasIndexValidator;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.typeahead.DefaultTypeaheadService.Configuration;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
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
    SaasIndexValidator.getInstance().validate(index);
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
        "language",
        "domain",
        "scope",
        "repository_path_url",
        "page_type_str",
        "publication_date",
        "products_mstr",
        "categories_mstr",
        "categories_murl",
        "tags_mstr",
        "start_date",
        "end_date",
        "title_str",
        "brand_str",
        "parent_path_url",
        "product_filter_paths_murl"
    };

  }
}
