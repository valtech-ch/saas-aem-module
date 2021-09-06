package com.valtech.aemsaas.core.services.search.impl;

import com.google.gson.JsonObject;
import com.valtech.aemsaas.core.models.request.SearchRequest;
import com.valtech.aemsaas.core.models.response.search.SearchResponse;
import com.valtech.aemsaas.core.services.search.SearchRequestExecutorService;
import com.valtech.aemsaas.core.services.search.SearchServiceConnectionConfigurationService;
import com.valtech.aemsaas.core.utils.HttpHostResolver;
import com.valtech.aemsaas.core.utils.HttpResponseParser;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.ssl.SSLContextBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

@Slf4j
@Component(name = "Search as a Service - Search Request Executor Service",
    service = SearchRequestExecutorService.class)
public class SearchRequestExecutorServiceImpl implements SearchRequestExecutorService {

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  @Reference
  private HttpClientBuilderFactory httpClientBuilderFactory;

  private HttpClientBuilder httpClientBuilder;

  @Override
  public Optional<SearchResponse> execute(@NonNull SearchRequest searchRequest) {
    HttpUriRequest request = searchRequest.getRequest();
    CloseableHttpClient client = httpClientBuilder.build();
    CloseableHttpResponse response = null;
    SearchResponse searchResponse = null;
    try {
      CloseableHttpClient httpClient = httpClientBuilder.build();
      response = httpClient.execute(request);
      log.info("Executing {} request on search api {}", request.getMethod(), request.getURI());
      log.info("Status Code: {}", response.getStatusLine().getStatusCode());
      log.debug("Reason: {}", response.getStatusLine().getReasonPhrase());
      if (HttpServletResponse.SC_OK == response.getStatusLine().getStatusCode()) {
        HttpResponseParser httpResponseParser = new HttpResponseParser(response);
        if (log.isDebugEnabled()) {
          log.debug("Response content: {}", httpResponseParser.getContentString());
        }
        searchResponse = new SearchResponse(new HttpResponseParser(response).toGsonModel(JsonObject.class));
      }
    } catch (IOException e) {
      log.error("Error while executing request", e);
    } finally {
      if (response != null) {
        IOUtils.closeQuietly(response, e -> log.error("Could not close response.", e));
        IOUtils.closeQuietly(client, e -> log.error("Could not close client.", e));
      }
    }

    return Optional.ofNullable(searchResponse);
  }

  @Activate
  @Modified
  private void activate() {
    log.debug("Configuring new Http Client Builder for Search Service requests.");
    RequestConfig requestConfig = createRequestConfig();
    log.debug("Http Client will be built with following request configuration {}", requestConfig);
    httpClientBuilder = httpClientBuilderFactory.newBuilder()
        .setDefaultRequestConfig(requestConfig);
    if (searchServiceConnectionConfigurationService.isBasicAuthenticationEnabled()) {
      log.debug("Basic Authentication is enabled.");
      getCredentialsProvider().ifPresent(credentialsProvider -> {
        log.debug("Setting basic authentication details for the http client.");
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
      });
    }
    if (searchServiceConnectionConfigurationService.isIgnoreSslEnabled()) {
      log.warn("Initializing HttpService with ignoring SSL Certificate");
      setToIgnoredSsl(httpClientBuilder);
    }
  }

  private RequestConfig createRequestConfig() {
    return RequestConfig.custom()
        .setConnectTimeout(searchServiceConnectionConfigurationService.getHttpConnectionTimeout())
        .setConnectionRequestTimeout(searchServiceConnectionConfigurationService.getHttpConnectionTimeout())
        .setSocketTimeout(searchServiceConnectionConfigurationService.getHttpSocketTimeout())
        .build();
  }

  private Optional<CredentialsProvider> getCredentialsProvider() {
    return new HttpHostResolver(searchServiceConnectionConfigurationService.getBaseUrl()).getHost()
        .map(httpHost -> {
          BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
          AuthScope authScope = new AuthScope(httpHost);
          UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(
              searchServiceConnectionConfigurationService.getBasicAuthenticationUser(),
              searchServiceConnectionConfigurationService.getBasicAuthenticationPassword());
          log.debug("Creating basic credentials provider with authScope: {}, user: {}", authScope,
              usernamePasswordCredentials);
          basicCredentialsProvider.setCredentials(authScope, usernamePasswordCredentials
          );
          return basicCredentialsProvider;
        });
  }

  private void setToIgnoredSsl(HttpClientBuilder httpClientBuilder) {
    try {
      SSLContextBuilder builder = new SSLContextBuilder();
      builder.loadTrustMaterial(null, (chain, authType) -> true);
      SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
      httpClientBuilder.setSSLSocketFactory(sslsf);
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      log.error("Error occurred while setting ignore ssl flag.", e);
    }
  }

}
