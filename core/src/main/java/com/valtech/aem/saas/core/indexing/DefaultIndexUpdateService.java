package com.valtech.aem.saas.core.indexing;

import com.google.gson.Gson;
import com.valtech.aem.saas.api.caconfig.SearchCAConfigurationModel;
import com.valtech.aem.saas.api.indexing.IndexUpdateService;
import com.valtech.aem.saas.api.indexing.dto.IndexContentPayloadDTO;
import com.valtech.aem.saas.api.indexing.dto.IndexUpdateResponseDTO;
import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.client.SearchRequestExecutorService;
import com.valtech.aem.saas.core.http.client.SearchServiceConnectionConfigurationService;
import com.valtech.aem.saas.core.http.request.SearchRequestDelete;
import com.valtech.aem.saas.core.http.request.SearchRequestPost;
import com.valtech.aem.saas.core.http.response.DefaultIndexUpdateDataExtractionStrategy;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import com.valtech.aem.saas.core.indexing.DefaultIndexUpdateService.Configuration;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
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
  public Optional<IndexUpdateResponseDTO> indexUrl(@NonNull SearchCAConfigurationModel searchConfiguration,
      @NonNull String url,
      @NonNull String repositoryPath) {
    return indexUrl(searchConfiguration.getClient(), url, repositoryPath);
  }

  @Override
  public Optional<IndexUpdateResponseDTO> deleteIndexedUrl(@NonNull SearchCAConfigurationModel searchConfiguration,
      @NonNull String url,
      @NonNull String repositoryPath) {
    return deleteIndexedUrl(searchConfiguration.getClient(), url, repositoryPath);
  }

  @Override
  public Optional<IndexUpdateResponseDTO> indexContent(@NonNull SearchCAConfigurationModel searchConfiguration,
      @NonNull IndexContentPayloadDTO indexContentPayloadDto) {
    return indexContent(searchConfiguration.getClient(), indexContentPayloadDto);
  }

  @Override
  public Optional<IndexUpdateResponseDTO> indexUrl(@NonNull String client, @NonNull String url,
      @NonNull String repositoryPath) {
    validateInput(client, url, repositoryPath);

    SearchRequest searchRequest = SearchRequestPost.builder()
        .uri(getRequestUri(client, configuration.indexUpdateService_apiIndexTriggerAction()))
        .httpEntity(createIndexUpdatePayloadEntity(url, repositoryPath))
        .build();

    return searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new DefaultIndexUpdateDataExtractionStrategy()));
  }

  @Override
  public Optional<IndexUpdateResponseDTO> deleteIndexedUrl(@NonNull String client, @NonNull String url,
      @NonNull String repositoryPath) {
    validateInput(client, url, repositoryPath);
    SearchRequest searchRequest = SearchRequestDelete.builder()
        .uri(getRequestUri(client, configuration.indexUpdateService_apiIndexTriggerAction()))
        .httpEntity(createIndexUpdatePayloadEntity(url, repositoryPath))
        .build();
    return searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new DefaultIndexUpdateDataExtractionStrategy()));
  }

  @Override
  public Optional<IndexUpdateResponseDTO> indexContent(@NonNull String client,
      @NonNull IndexContentPayloadDTO indexContentPayloadDto) {
    validateInputClient(client);
    SearchRequest searchRequest = SearchRequestPost.builder()
        .uri(getRequestUri(client, configuration.indexUpdateService_apiPushContentAction()))
        .httpEntity(createIndexContentPayloadEntity(indexContentPayloadDto))
        .build();

    return searchRequestExecutorService.execute(searchRequest)
        .filter(SearchResponse::isSuccess)
        .flatMap(response -> response.get(new DefaultIndexUpdateDataExtractionStrategy()));
  }

  private HttpEntity createIndexUpdatePayloadEntity(@NonNull String url, @NonNull String repositoryPath) {
    return EntityBuilder.create()
        .setParameters(new BasicNameValuePair(REQUEST_PARAMETER_URL, url),
            new BasicNameValuePair(REQUEST_PARAMETER_REPOSITORY_PATH, repositoryPath))
        .setContentEncoding(StandardCharsets.UTF_8.name())
        .build();
  }

  private HttpEntity createIndexContentPayloadEntity(IndexContentPayloadDTO indexContentPayloadDto) {
    return EntityBuilder.create()
        .setText(new Gson().toJson(indexContentPayloadDto))
        .setContentType(ContentType.APPLICATION_JSON)
        .setContentEncoding(StandardCharsets.UTF_8.name())
        .build();
  }

  private String getRequestUri(String client, String action) {
    return String.format("%s%s/%s%s%s",
        searchServiceConnectionConfigurationService.getBaseUrl(),
        configuration.indexUpdateService_apiBasePath(),
        client,
        configuration.indexUpdateService_apiVersionPath(),
        action);
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
    String DEFAULT_API_BASE_PATH = "/admin"; // NOSONAR
    String DEFAULT_API_VERSION_PATH = "/api/v3"; // NOSONAR

    @AttributeDefinition(name = "Api base path",
        description = "Base path of the api",
        type = AttributeType.STRING)
    String indexUpdateService_apiBasePath() default DEFAULT_API_BASE_PATH; // NOSONAR

    @AttributeDefinition(name = "Api version path",
        description = "Path designating the api version",
        type = AttributeType.STRING)
    String indexUpdateService_apiVersionPath() default DEFAULT_API_VERSION_PATH; // NOSONAR

    @AttributeDefinition(name = "Api index trigger action",
        description = "Path designating the index trigger action",
        type = AttributeType.STRING)
    String indexUpdateService_apiIndexTriggerAction() default DEFAULT_API_INDEX_TRIGGER_ACTION; // NOSONAR

    @AttributeDefinition(name = "Api push content action",
        description = "Path designating the push content action",
        type = AttributeType.STRING)
    String indexUpdateService_apiPushContentAction() default DEFAULT_API_PUSH_CONTENT_ACTION; // NOSONAR

  }
}
