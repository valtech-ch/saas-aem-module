package com.valtech.aem.saas.core.http.client;

import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
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
import org.osgi.service.component.annotations.*;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static com.valtech.aem.saas.core.http.client.DefaultSearchAdminRequestExecutorService.CONFIGURATION_PID;

@Slf4j
@Component(name = "Search as a Service - Search Admin Request Executor Service",
        service = SearchAdminRequestExecutorService.class,
        immediate = true,
        configurationPid = CONFIGURATION_PID)
@Designate(ocd = DefaultSearchAdminRequestExecutorService.Configuration.class)
public class DefaultSearchAdminRequestExecutorService implements SearchAdminRequestExecutorService {

    static final String CONFIGURATION_PID = "com.valtech.aem.saas.core.http.client.DefaultSearchAdminRequestExecutorService";

    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    @Getter(value = AccessLevel.PRIVATE, onMethod_ = {@Synchronized})
    private SearchServiceConnectionConfigurationService searchConnectionConfig;

    private CloseableHttpClient httpClient;

    private Configuration configuration;

    @Reference
    @Synchronized
    protected void bindSearchConnectionConfig(SearchServiceConnectionConfigurationService service) {
        this.searchConnectionConfig = service;
    }

    @Synchronized
    protected void updatedSearchConnectionConfig(SearchServiceConnectionConfigurationService service) {
        this.searchConnectionConfig = service;
        initHttpClient();
    }

    @Override
    public String getBaseUrl() {
        return configuration.searchAdminRequestExecutorService_baseurl();
    }

    @Override
    public Optional<SearchResponse> execute(@NonNull SearchRequest searchRequest) {
        return new SearchRequestExecutor(httpClient).execute(searchRequest);

    }

    @Activate
    @Modified
    private void activate(Configuration configuration) {
        this.configuration = configuration;
        initHttpClient();
    }

    private void initHttpClient() {
        log.debug("Configuring new Http Client Builder for Search Service requests.");
        RequestConfig requestConfig = createRequestConfig();
        log.debug("Http Client will be built with following request configuration {}", requestConfig);
        HttpClientBuilder httpClientBuilder = httpClientBuilderFactory.newBuilder()
                .setDefaultRequestConfig(requestConfig);
        if (configuration.searchAdminRequestExecutorService_jwtAuthentication_enable()) {
            httpClientBuilder.setRequestExecutor(new JWTHttpRequestExecutor(configuration.searchAdminRequestExecutorService_jwtAuthentication_token()));
        } else if (configuration.searchAdminRequestExecutorService_basicAuthentication_enable()) {
            log.debug("Basic Authentication is enabled.");
            getCredentialsProvider(configuration).ifPresent(credentialsProvider -> {
                log.debug("Setting basic authentication details for the http client.");
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            });
        }
        if (getSearchConnectionConfig().isIgnoreSslEnabled()) {
            log.warn("Initializing HttpService with ignoring SSL Certificate");
            setToIgnoredSsl(httpClientBuilder);
        }
        httpClientBuilder.setMaxConnTotal(getSearchConnectionConfig().getHttpMaxTotalConnections());
        httpClientBuilder.setMaxConnPerRoute(getSearchConnectionConfig().getHttpMaxConnectionsPerRoute());
        httpClient = httpClientBuilder.build();
    }

    @Deactivate
    private void deactivate() {
        IOUtils.closeQuietly(httpClient, e -> log.error("Could not close client.", e));
    }

    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(getSearchConnectionConfig().getHttpConnectionTimeout())
                .setConnectionRequestTimeout(getSearchConnectionConfig().getHttpConnectionTimeout())
                .setSocketTimeout(getSearchConnectionConfig().getHttpSocketTimeout())
                .build();
    }

    private Optional<CredentialsProvider> getCredentialsProvider(Configuration configuration) {
        return new HttpHostResolver(configuration.searchAdminRequestExecutorService_baseurl()).getHost()
                .map(httpHost -> {
                    BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
                    AuthScope authScope = new AuthScope(
                            httpHost);
                    UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(
                            configuration.searchAdminRequestExecutorService_basicAuthentication_user(),
                            configuration.searchAdminRequestExecutorService_basicAuthentication_password());
                    log.debug(
                            "Creating basic credentials provider with authScope: {}, user: {}",
                            authScope,
                                                                                         usernamePasswordCredentials);
                                                                                 basicCredentialsProvider.setCredentials(
                                                                                         authScope,
                                                                                         usernamePasswordCredentials
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


    @ObjectClassDefinition(name = "Search as a Service - Search Admin Request Executor Service Configuration",
            description = "URL and authentication details for connect to Search Admin Endpoint.")
    public @interface Configuration {

        String DEFAULT_WEB_SERVICE_URL = "https://ic-test-search-admin.valtech.swiss/admin";
        boolean DEFAULT_BASIC_AUTHENTICATION_ENABLE = false;
        boolean DEFAULT_JWT_AUTHENTICATION_ENABLE = false;

        @AttributeDefinition(name = "Base URL",
                description = "The protocol + url for the search service")
        String searchAdminRequestExecutorService_baseurl() default DEFAULT_WEB_SERVICE_URL; // NOSONAR

        @AttributeDefinition(name = "Basic authentication - User",
                description = "User for basic authentication to the search service")
        String searchAdminRequestExecutorService_basicAuthentication_user(); // NOSONAR

        @AttributeDefinition(name = "Basic authentication - Password",
                description = "Password for basic authentication to the search service",
                type = AttributeType.PASSWORD)
        String searchAdminRequestExecutorService_basicAuthentication_password(); // NOSONAR

        @AttributeDefinition(name = "Use basic authentication",
                description = "Submit the above user + password to the search service",
                type = AttributeType.BOOLEAN)
        boolean searchAdminRequestExecutorService_basicAuthentication_enable() default DEFAULT_BASIC_AUTHENTICATION_ENABLE; // NOSONAR

        @AttributeDefinition(name = "JWT authentication token",
                description = "Token string",
                type = AttributeType.PASSWORD)
        String searchAdminRequestExecutorService_jwtAuthentication_token(); // NOSONAR

        @AttributeDefinition(name = "Use JWT authentication",
                description = "Set above authorization token in search service request.",
                type = AttributeType.BOOLEAN)
        boolean searchAdminRequestExecutorService_jwtAuthentication_enable() default DEFAULT_JWT_AUTHENTICATION_ENABLE; // NOSONAR

    }
}
