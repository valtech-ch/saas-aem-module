package com.valtech.aemsaas.core.services.impl;

import com.valtech.aemsaas.core.function.HttpResponseConsumer;
import com.valtech.aemsaas.core.models.request.SearchRequest;
import com.valtech.aemsaas.core.services.SearchRequestExecutorService;
import com.valtech.aemsaas.core.services.SearchServiceConnectionConfigurationService;
import com.valtech.aemsaas.core.utils.HttpHostResolver;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
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
@Component(name = "VALTECH - Search Request Executor Service",
    service = SearchRequestExecutorService.class)
public class SearchRequestExecutorServiceImpl implements SearchRequestExecutorService {

  @Reference
  private SearchServiceConnectionConfigurationService searchServiceConnectionConfigurationService;

  @Reference
  private HttpClientBuilderFactory httpClientBuilderFactory;

  private HttpClientBuilder httpClientBuilder;

  @Override
  public <R> R execute(@NonNull SearchRequest searchRequest, @NonNull HttpResponseConsumer<R> httpResponseConsumer) {
    HttpUriRequest request = searchRequest.getRequest();
    try (CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = httpClient.execute(request)) {
      log.info("Executing {} request on search api {}", request.getMethod(), request.getURI());
      log.info("Status Code: {}", response.getStatusLine().getStatusCode());
      log.debug("Reason: {}", response.getStatusLine().getReasonPhrase());
      if (HttpServletResponse.SC_OK == response.getStatusLine().getStatusCode()) {
        return httpResponseConsumer.apply(response);
      } else {
        log.error("Unable to consume response {}, status code is {}", request.getURI(),
            response.getStatusLine().getStatusCode());
      }
    } catch (IOException e) {
      log.error("Error while executing request", e);
    }
    return null;
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
      getCredentialsProvider().ifPresentOrElse(httpClientBuilder::setDefaultCredentialsProvider,
          () -> log.error("Failed to set credentials provider"));
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
          basicCredentialsProvider.setCredentials(new AuthScope(httpHost),
              new UsernamePasswordCredentials(searchServiceConnectionConfigurationService.getBasicAuthenticationUser(),
                  searchServiceConnectionConfigurationService.getBasicAuthenticationPassword()));
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
