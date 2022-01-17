package com.valtech.aem.saas.core.http.client;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.ssl.SSLContextBuilder;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Slf4j
@Builder
public final class SearchHttpClientFactory {
    public final HttpClientBuilderFactory httpClientBuilderFactory;
    private final int httpConnectionTimeout;
    private final int httpSocketTimeout;
    private final boolean jwtAuthenticationEnabled;
    private final String jwtToken;
    private final boolean basicAuthenticationEnabled;
    private final String baseUrl;
    private final String basicAuthenticationUser;
    private final String basicAuthenticationPassword;
    private final boolean ignoreSslEnabled;
    private final int httpMaxTotalConnections;
    private final int httpMaxConnectionsPerRoute;

    public CloseableHttpClient create() {
        log.debug("Configuring new Http Client Builder for Search Service requests.");
        RequestConfig requestConfig = createRequestConfig();
        log.debug("Http Client will be built with following request configuration {}", requestConfig);
        HttpClientBuilder httpClientBuilder = httpClientBuilderFactory.newBuilder()
                                                                      .setDefaultRequestConfig(requestConfig);
        if (jwtAuthenticationEnabled) {
            httpClientBuilder.setRequestExecutor(new JWTHttpRequestExecutor(jwtToken));
        } else if (basicAuthenticationEnabled) {
            log.debug("Basic Authentication is enabled.");
            getCredentialsProvider().ifPresent(credentialsProvider -> {
                log.debug("Setting basic authentication details for the http client.");
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }
        if (ignoreSslEnabled) {
            log.warn("Initializing HttpService with ignoring SSL Certificate");
            setToIgnoredSsl(httpClientBuilder);
        }
        httpClientBuilder.setMaxConnTotal(httpMaxTotalConnections);
        httpClientBuilder.setMaxConnPerRoute(httpMaxConnectionsPerRoute);
        return httpClientBuilder.build();
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                            .setConnectTimeout(httpConnectionTimeout)
                            .setConnectionRequestTimeout(httpConnectionTimeout)
                            .setSocketTimeout(httpSocketTimeout)
                            .build();
    }

    private Optional<CredentialsProvider> getCredentialsProvider() {
        return new HttpHostResolver(baseUrl)
                .getHost()
                .map(httpHost -> {
                    BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
                    AuthScope authScope = new AuthScope(
                            httpHost);
                    UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(
                            basicAuthenticationUser,
                            basicAuthenticationPassword);
                    log.debug("Creating basic credentials provider with authScope: {}, user: {}",
                              authScope,
                              usernamePasswordCredentials);
                    basicCredentialsProvider.setCredentials(authScope, usernamePasswordCredentials);
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
