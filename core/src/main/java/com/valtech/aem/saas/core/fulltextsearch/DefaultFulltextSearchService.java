package com.valtech.aem.saas.core.fulltextsearch;

import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConfiguration;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchConsumerService;
import com.valtech.aem.saas.api.fulltextsearch.FulltextSearchService;
import com.valtech.aem.saas.core.common.saas.SaasIndexValidator;
import com.valtech.aem.saas.core.fulltextsearch.DefaultFulltextSearchService.Configuration;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
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
@Component(name = "Search as a Service - Fulltext Search Service",
    service = {FulltextSearchService.class, FulltextSearchConfigurationService.class})
@Designate(ocd = Configuration.class)
public class DefaultFulltextSearchService implements
    FulltextSearchService<FulltextSearchConfiguration<IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder>>,
    FulltextSearchConfigurationService {

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  @Reference
  private SearchRequestExecutorService searchRequestExecutorService;

  private Configuration configuration;

  @Override
  public int getRowsMaxLimit() {
    return configuration.fulltextSearchService_rowsMaxLimit();
  }

  @Override
  public FulltextSearchConsumerService getFulltextSearchConsumerService(String index,
      FulltextSearchConfiguration<IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder> configuration) {
    SaasIndexValidator.getInstance().validate(index);
    IndexFulltextSearchConsumerService.IndexFulltextSearchConsumerServiceBuilder builder =
        IndexFulltextSearchConsumerService.builder()
            .searchRequestExecutorService(searchRequestExecutorService)
            .apiUrl(getApiUrl(index));
    return configuration != null
        ? configuration.apply(builder).build()
        : builder.build();
  }

  private String getApiUrl(String index) {
    return String.format("%s%s/%s%s",
        searchServiceConnectionConfigurationService.getBaseUrl(),
        configuration.fulltextSearchService_apiVersion(),
        index,
        configuration.fulltextSearchService_apiAction());
  }

  @Activate
  @Modified
  private void activate(Configuration configuration) {
    this.configuration = configuration;
  }

  @ObjectClassDefinition(name = "Search as a Service - Fulltext Search Service Configuration",
      description = "Fulltext Search Api specific details.")
  public @interface Configuration {

    int DEFAULT_ROWS_MAX_LIMIT = 9999;
    String DEFAULT_API_ACTION = "/search";
    String DEFAULT_API_VERSION_PATH = "/api/v3";

    @AttributeDefinition(name = "Api base path",
        description = "Api base path",
        type = AttributeType.STRING)
    String fulltextSearchService_apiVersion() default DEFAULT_API_VERSION_PATH;

    @AttributeDefinition(name = "Api action",
        description = "What kind of action should be defined",
        type = AttributeType.STRING)
    String fulltextSearchService_apiAction() default DEFAULT_API_ACTION;

    @AttributeDefinition(name = "Rows max limit.",
        description = "Maximum number of results per page allowed.",
        type = AttributeType.INTEGER)
    int fulltextSearchService_rowsMaxLimit() default DEFAULT_ROWS_MAX_LIMIT;

  }
}
