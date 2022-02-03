package com.valtech.aem.saas.core.http.client;

import com.valtech.aem.saas.api.request.SearchRequest;
import com.valtech.aem.saas.core.http.response.SearchResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.osgi.service.component.annotations.*;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

import java.util.Optional;

@Slf4j
@Component(service = SearchAdminRequestExecutorService.class,
           immediate = true)
@ServiceDescription("Search as a Service - Search Admin Request Executor Service")
@Designate(ocd = DefaultSearchAdminRequestExecutorService.Configuration.class)
public class DefaultSearchAdminRequestExecutorService implements SearchAdminRequestExecutorService {

    @Reference
    private HttpClientBuilderFactory httpClientBuilderFactory;

    @Getter(value = AccessLevel.PRIVATE,
            onMethod_ = {@Synchronized})
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
        httpClient = SearchHttpClientFactory.builder()
                                            .httpClientBuilderFactory(httpClientBuilderFactory)
                                            .httpConnectionTimeout(getSearchConnectionConfig().getHttpConnectionTimeout())
                                            .httpSocketTimeout(getSearchConnectionConfig().getHttpSocketTimeout())
                                            .jwtAuthenticationEnabled(configuration.searchAdminRequestExecutorService_jwtAuthentication_enable())
                                            .jwtToken(configuration.searchAdminRequestExecutorService_jwtAuthentication_token())
                                            .basicAuthenticationEnabled(configuration.searchAdminRequestExecutorService_basicAuthentication_enable())
                                            .baseUrl(configuration.searchAdminRequestExecutorService_baseurl())
                                            .basicAuthenticationUser(configuration.searchAdminRequestExecutorService_basicAuthentication_user())
                                            .basicAuthenticationUser(configuration.searchAdminRequestExecutorService_basicAuthentication_password())
                                            .ignoreSslEnabled(getSearchConnectionConfig().isIgnoreSslEnabled())
                                            .httpMaxTotalConnections(getSearchConnectionConfig().getHttpMaxTotalConnections())
                                            .httpMaxConnectionsPerRoute(getSearchConnectionConfig().getHttpMaxConnectionsPerRoute())
                                            .build()
                                            .create();
    }

    @Deactivate
    private void deactivate() {
        IOUtils.closeQuietly(httpClient, e -> log.error("Could not close client.", e));
    }

    @ObjectClassDefinition(name = "Search as a Service - Search Admin Request Executor Service Configuration",
                           description = "URL and authentication details for connect to Search Admin Endpoint.")
    public @interface Configuration {

        String DEFAULT_WEB_SERVICE_URL = "https://ic-search-admin.valtech.swiss/admin";
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
