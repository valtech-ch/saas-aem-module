package com.valtech.aem.saas.core.indexing;

import com.google.gson.Gson;
import com.valtech.aem.saas.api.indexing.IndexContentPayload;
import com.valtech.aem.saas.api.indexing.IndexUpdateResponse;
import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.request.SearchRequest;
import com.valtech.aem.saas.core.http.request.SearchRequestDelete;
import com.valtech.aem.saas.core.http.request.SearchRequestPost;
import com.valtech.aem.saas.core.http.response.DefaultIndexUpdateDataExtractionStrategy;
import com.valtech.aem.saas.core.indexing.DefaultIndexUpdateService.Configuration;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@Slf4j
@Component(name = "Search as a Service - Index Update Service",
    service = IndexUpdateService.class)
@Designate(ocd = Configuration.class)
public class DefaultIndexUpdateService implements IndexUpdateService {

  public static final String REQUEST_PARAMETER_URL = "url";
  public static final String REQUEST_PARAMETER_REPOSITORY_PATH = "repository_path";

  @Reference
  private SearchRequestExecutorService searchRequestExecutorService;

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  private Configuration configuration;

  @Activate
  @Modified
  private void activate(Configuration configuration) {
    this.configuration = configuration;
  }

  @Override
  public Optional<IndexUpdateResponse> indexUrl(@NonNull String client, @NonNull String url,
      @NonNull String repositoryPath) {
    validateInput(client, url, repositoryPath);

    SearchRequest searchRequest = SearchRequestPost.builder()
        .uri(getRequestUri(client, configuration.indexUpdateService_apiIndexTriggerAction()))
        .httpEntity(EntityBuilder.create().setParameters(new BasicNameValuePair(REQUEST_PARAMETER_URL, url),
                new BasicNameValuePair(REQUEST_PARAMETER_REPOSITORY_PATH, repositoryPath))
            .setContentEncoding(StandardCharsets.UTF_8.name())
            .build())
        .build();

    return searchRequestExecutorService.execute(searchRequest)
        .flatMap(response -> response.get(new DefaultIndexUpdateDataExtractionStrategy()));
  }

  @Override
  public Optional<IndexUpdateResponse> deleteIndexedUrl(@NonNull String client, @NonNull String url,
      @NonNull String repositoryPath) {
    validateInput(client, url, repositoryPath);
    SearchRequest searchRequest = SearchRequestDelete.builder()
        .uri(getRequestUri(client, configuration.indexUpdateService_apiIndexTriggerAction()))
        .httpEntity(EntityBuilder.create().setParameters(new BasicNameValuePair(REQUEST_PARAMETER_URL, url),
                new BasicNameValuePair(REQUEST_PARAMETER_REPOSITORY_PATH, repositoryPath))
            .setContentEncoding(StandardCharsets.UTF_8.name())
            .build())
        .build();
    return searchRequestExecutorService.execute(searchRequest)
        .flatMap(response -> response.get(new DefaultIndexUpdateDataExtractionStrategy()));
  }

  @Override
  public Optional<IndexUpdateResponse> indexContent(@NonNull String client,
      @NonNull IndexContentPayload indexContentPayload) {
    validateInputClient(client);
    SearchRequest searchRequest = SearchRequestPost.builder()
        .uri(getRequestUri(client, configuration.indexUpdateService_apiPushContentAction()))
        .httpEntity(EntityBuilder.create().setText(new Gson().toJson(indexContentPayload))
            .setContentType(ContentType.APPLICATION_JSON)
            .setContentEncoding(StandardCharsets.UTF_8.name())
            .build())
        .build();

    return searchRequestExecutorService.execute(searchRequest)
        .flatMap(response -> response.get(new DefaultIndexUpdateDataExtractionStrategy()));
  }

  private String getRequestUri(String client, String action) {
    return String.format("%s%s%s%s%s", searchServiceConnectionConfigurationService.getBaseUrl(),
        configuration.indexUpdateService_apiBasePath(), client,
        configuration.indexUpdateService_apiVersionPath(), action);
  }

  private void validateInput(String client, String url, String repositoryPath) {
    validateInputClient(client);
    if (StringUtils.isBlank(url)) {
      throw new IllegalArgumentException("Please pass a url of a content that should be indexed.");
    }
    if (StringUtils.isBlank(repositoryPath)) {
      throw new IllegalArgumentException("Please pass a repository path regex value.");
    }
  }

  private void validateInputClient(String client) {
    if (StringUtils.isBlank(client)) {
      throw new IllegalArgumentException("Please configure client in context aware configuration.");
    }
  }

  @ObjectClassDefinition(name = "Search as a Service - Index Update Service Configuration",
      description = "Index Update Api specific details.")
  public @interface Configuration {

    String DEFAULT_API_INDEX_TRIGGER_ACTION = "/index/trigger";
    String DEFAULT_API_PUSH_CONTENT_ACTION = "/content";
    String DEFAULT_API_BASE_PATH = "/admin";
    String DEFAULT_API_VERSION_PATH = "/api/v3";

    @AttributeDefinition(name = "Api base path",
        description = "Api base path",
        type = AttributeType.STRING)
    String indexUpdateService_apiBasePath() default DEFAULT_API_BASE_PATH;

    @AttributeDefinition(name = "Api version path",
        description = "Api base path",
        type = AttributeType.STRING)
    String indexUpdateService_apiVersionPath() default DEFAULT_API_VERSION_PATH;

    @AttributeDefinition(name = "Api index trigger action",
        description = "What kind of action should be defined",
        type = AttributeType.STRING)
    String indexUpdateService_apiIndexTriggerAction() default DEFAULT_API_INDEX_TRIGGER_ACTION;

    @AttributeDefinition(name = "Api action",
        description = "What kind of action should be defined",
        type = AttributeType.STRING)
    String indexUpdateService_apiPushContentAction() default DEFAULT_API_PUSH_CONTENT_ACTION;

  }
}
